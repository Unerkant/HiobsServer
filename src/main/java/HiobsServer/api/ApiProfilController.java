package HiobsServer.api;

import HiobsServer.dto.ProfilNameUpdate;
import HiobsServer.dto.ProfileBildDelete;
import HiobsServer.model.Channel;
import HiobsServer.model.User;
import HiobsServer.repository.ChannelRepository;
import HiobsServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;


/**
 * Den 28.05.2025
 */

@RestController
public class ApiProfilController {

    private final Path targetDir = Paths.get("profilbild");
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;

    /**
     *  PROFIL BILD AUS DEN ORDNER HiobsServer/profilbild LADEN
     *  ACHTUNG: nur PNG Bilder, keine svg
     *
     *  der @GetMapping("/profilbild/{imageName}") generiert und gibt der Bild-Link zurück, dass
     *  ermöglicht das profil Bild anzuzeigen in alle vorhandenen App wie HiobsServer oder
     *  HiobsClient oder HiobsWeb, weil von Außen es ist unmöglich einen zugriff auf resources datei
     *  wie resources/static/profilbild
     *
     *  BEMERKUNG: bei erstellung von profil Bilder, wird die Bildnamen von User Token genommen + png format
     *
     *  1. Anzeigen das profil Bild in HiobsServer:
     *      Controller: String imageName = "123456789";
     *                  model.addAttribute("bildName", imageName);
     *            HTML: <img data-th-src="@{ ${'http://localhost:8080/profilbild/'+bildName+'.png'} }"
     *                                          class="imgCircle" decoding="async" draggable="false">
     *                  oder (ohne http, aber nur bei HiobsServer)
     *                  <img data-th-src="@{ ${'profilbild/'+bildName+'.png'} }"
     *                       class="imgCircle" decoding="async" draggable="false">
     *  2.  Anzeige das profil Bild in HiobsClient:
     *      public final String SERVER_PROFILBILD       = "http://localhost:8080/profilbild/";
     *      Controller: String profilbildURL = webConfig.SERVER_PROFILBILD;
     *                  model.addAttribute("profilBildUrl", profilbildURL);
     *            HTML:<div data-th-if="${freundeDaten != null}" data-th-each="friends : ${freundeDaten}">
     *                      <img data-th-src="@{ ${profilBildUrl+bildName+'.png'} }"
     *                                          class="imgCircle" decoding="async" draggable="false">
     *                 </div>
     *
     * @param   bildName
     * @return  Bild Adresse
     * @throws IOException
     */

    @GetMapping(value = "/profil/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] bildLaden(@PathVariable(value = "imageName", required = true) String bildName)
                                throws IOException {
        /**
         * Verwenden Sie ClassPathResource, um die Datei zu finden, unabhängig davon,
         * ob sie im Dateisystem oder in einer JAR-Datei ist.
         */
        // Pfad zum 'profilbild'-Ordner im Wurzelverzeichnis bauen
        Path imagePath = targetDir.resolve(bildName);

        // Prüfen, ob die Datei existiert
        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            //System.err.println("Bild nicht gefunden: " + imagePath.toAbsolutePath());
            return null;
        }

    }


    /**
     * REST-Endpunkt für direkte Updates aus den Client-Settings (User-Profilbild oder Kanal-Logo)
     */

    @PostMapping(path = "/profil/bildUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadViaRest(@RequestParam("file") MultipartFile file,
                                                @RequestParam("id") String id,
                                                @RequestParam(value = "targetType", defaultValue = "USER") String targetType) {
        boolean success = bildUpload(file, id, targetType);
        if (success) {
            return ResponseEntity.ok("Bild erfolgreich aktualisiert.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Speichern des Bildes.");
    }

    /**
     * Zentrale Methode: Speichert die Datei und aktualisiert die MongoDB-Collection
     */

    public boolean bildUpload(MultipartFile file, String imageId, String targetType) {
        if (file == null || file.isEmpty() || imageId == null || imageId.isEmpty()) {
            return false;
        }

        try {
            // 1. Ordner prüfen/erstellen
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            // 2. Bild im Wurzelordner speichern / überschreiben
            Path targetFile = targetDir.resolve(imageId + ".png");
            Files.write(targetFile, file.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            //System.out.println("Bild gespeichert unter: " + targetFile.toAbsolutePath());

            // 3. Datenbank-Update basierend auf der Rolle (targetType)
            if ("USER".equalsIgnoreCase(targetType)) {
                Optional<User> userOpt = userRepository.findById(imageId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    user.setProfilePicture(imageId); // Name/ID des Bildes eintragen
                    userRepository.save(user);
                    //System.out.println("User DB-Update erfolgreich für Token/ID: " + imageId);
                }
            } else if ("CHANNEL".equalsIgnoreCase(targetType)) {
                Optional<Channel> channelOpt = channelRepository.findById(imageId);
                if (channelOpt.isPresent()) {
                    Channel channel = channelOpt.get();
                    channel.setChannelPicture(imageId); // Name/ID des Logos eintragen
                    channelRepository.save(channel);
                    //System.out.println("Channel DB-Update erfolgreich für ID: " + imageId);
                }
            }

            return true;

        } catch (IOException e) {
            //System.err.println("Fehler beim Speichern des Bildes (" + imageId + "): " + e.getMessage());
            return false;
        }
    }

    /**
     * das Profilbild oder channel-logo aus den Ordner 'profilbild' Löschen und aus den collection
     * users & channels von spalte profilePicture entfernen
     */

    @PostMapping(path = "/profil/bildDelete")
    public ResponseEntity<String> removeViaRest(@RequestBody ProfileBildDelete profileBildDelete) {

        if (profileBildDelete == null || profileBildDelete.bildName() == null || profileBildDelete.bildName().isEmpty()) {
            return ResponseEntity.badRequest().body("Fehlende Parameter für die Löschung.");
        }

        String bildName = profileBildDelete.bildName();
        String targetType = profileBildDelete.targetType();

        //System.out.println("Löschanfrage für Bild/ID: " + bildName + " | Typ: " + targetType);

        boolean success = bildLoeschen(bildName, targetType);

        if (success) {
            return ResponseEntity.ok("Bild und Datenbank-Eintrag erfolgreich gelöscht.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Löschen des Bildes oder des DB-Eintrags.");
        }
    }

    /**
     * Hilfsmethode: Entfernt den Eintrag aus MongoDB und löscht die Datei auf der Festplatte
     */

    public boolean bildLoeschen(String imageId, String targetType) {
        try {
            // 1. Eintrag in MongoDB löschen (Feld profilePicture / channelPicture auf null oder "" setzen)
            if ("USER".equalsIgnoreCase(targetType)) {
                Optional<User> userOpt = userRepository.findById(imageId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    user.setProfilePicture(null); // Oder "" je nach deiner Model-Logik
                    userRepository.save(user);
                    //System.out.println("ProfilePicture aus User DB gelöscht.");
                }
            } else if ("CHANNEL".equalsIgnoreCase(targetType)) {
                Optional<Channel> channelOpt = channelRepository.findById(imageId);
                if (channelOpt.isPresent()) {
                    Channel channel = channelOpt.get();
                    channel.setChannelPicture(null); // Oder "" je nach deiner Model-Logik
                    channelRepository.save(channel);
                    //System.out.println("ChannelPicture aus Channel DB gelöscht.");
                }
            }

            // 2. Bilddatei im Ordner 'profilbild' löschen
            Path imagePath = Paths.get("profilbild").resolve(imageId + ".png");

            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
                //System.out.println("Datei erfolgreich auf Festplatte gelöscht: " + imagePath.toAbsolutePath());
            } else {
                //System.out.println("Hinweis: Datei existierte nicht auf der Festplatte (" + imagePath.toString() + ").");
            }

            return true;

        } catch (IOException e) {
            //System.err.println("Fehler beim Löschen der Bilddatei (" + imageId + "): " + e.getMessage());
            return false;
        } catch (Exception e) {
            //System.err.println("Allgemeiner Fehler beim Löschen (" + imageId + "): " + e.getMessage());
            return false;
        }
    }

    /* ************************* User Name + Vorname anlegen/ändern ************************** */

    @PostMapping(path = "/profil/nameUpdate")
    public ResponseEntity<String> nameUpdate(@RequestBody ProfilNameUpdate profilNameUpdate) {

        // 1. Sicherheitscheck: Record darf nicht null sein
        if (profilNameUpdate == null) {
            //System.err.println("Profil-Update abgebrochen: Payload ist null.");
            return ResponseEntity.ok("nichtgespeichert");
        }

        String userId = profilNameUpdate.usersId(); // ID/Token des Users aus dem Record
        String userName = profilNameUpdate.usersName();
        String userVorname = profilNameUpdate.usersVorname();

        //System.out.println("Namen-Update Anforderung für ID (" + userId + "): Vorname=" + userVorname + ", Nachname=" + userName);

        // 2. Prüfen, ob eine User-ID übergeben wurde
        if (userId == null || userId.trim().isEmpty()) {
            //System.err.println("Profil-Update abgebrochen: Keine User-ID angegeben.");
            return ResponseEntity.ok("nichtgespeichert");
        }

        try {
            // 3. User aus der MongoDB-Collection 'users' laden
            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // 4. Namensfelder verarbeiten (falls übergeben, trimmen; falls null, leerer String)
                String neuerNachname = (userName != null) ? userName.trim() : "";
                String neuerVorname = (userVorname != null) ? userVorname.trim() : "";

                // 5. Felder im User-Model aktualisieren
                user.setUsername(neuerNachname);
                user.setUservorname(neuerVorname);

                // 6. In MongoDB speichern (Aktualisiert bestehenden Datensatz)
                userRepository.save(user);

                //System.out.println("Name erfolgreich aktualisiert für User ID: " + userId);
                return ResponseEntity.ok("gespeichert");

            } else {
                //System.err.println("User mit ID " + userId + " wurde in der Datenbank nicht gefunden.");
                return ResponseEntity.ok("nichtgespeichert");
            }

        } catch (Exception e) {
            //System.err.println("Fehler beim Aktualisieren des Namens: " + e.getMessage());
            return ResponseEntity.ok("nichtgespeichert");
        }
    }
}
