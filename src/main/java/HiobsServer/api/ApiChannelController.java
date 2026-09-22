package HiobsServer.api;

import HiobsServer.model.Channel;
import HiobsServer.model.User;
import HiobsServer.repository.ChannelRepository;
import HiobsServer.repository.UserRepository;
import HiobsServer.service.UserService;
import HiobsServer.utilities.MyUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Den 13.08.2026
 */

@RestController
public class ApiChannelController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUtilities  myUtilities;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ApiProfilController apiProfilController;

    private final MongoTemplate mongoTemplate;
    public ApiChannelController(MongoTemplate mongoTemplate) { this.mongoTemplate = mongoTemplate; }


    /**
     * Alle meine Channels aus den MongoDB lesen und an
     * HiobsClient/SettingController(7. categories) zurücksenden
     * @param ownerId
     * @return
     */
    @PostMapping(path = "/channel/myChannels")
    public ResponseEntity<List<Channel>> getAllChannels(
                        @RequestHeader(value = "ownerID", required = false) String ownerId) {

        // 1. Bereinigen der gesendeten ID (entfernt evtl. Anführungszeichen oder Leerzeichen)
        String cleanOwnerId = (ownerId != null) ? ownerId.replace("\"", "").trim() : "";

        //System.out.println("Suche Kanäle für Owner-ID: " + cleanOwnerId);

        if (cleanOwnerId.isEmpty()) {
            //System.err.println("Abbruch: Keine gültige Owner-ID übergeben.");
            return new ResponseEntity<>(List.of(), HttpStatus.BAD_REQUEST);
        }

        try {
            // 2. Alle Kanäle aus der Collection 'channels' über das Repository suchen
            List<Channel> channels = channelRepository.findByOwnerId(cleanOwnerId);

            //System.out.println("Gefundene Kanäle: " + channels.size());

            // 3. Trefferliste an den HiobsClient zurückgeben
            return new ResponseEntity<>(channels, HttpStatus.OK);

        } catch (Exception e) {
            //System.err.println("Fehler beim Abrufen der Kanäle: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Neuer Kanal Erstellen
     *
     * @param meId
     * @param channelName
     * @param channelBeschreibung
     * @param file
     * @return
     */
    @PostMapping(path = "/channel/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addChannel(
            @RequestParam("me") String meId,
            @RequestParam("channel") String channelName,
            @RequestParam(value = "channeltext", required = false, defaultValue = "") String channelBeschreibung,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // A. Eindeutige ID für den Kanal generieren
            String recipientId = myUtilities.channelToken();

            // B. Channel-Objekt anlegen
            Channel newChannel = new Channel(recipientId, channelName.trim(), channelBeschreibung.trim(),
                                                                meId, Instant.now(), null);

            // C. Variante 3 prüfen: Ist ein echtes Bild mitgekommen?
            if (file != null && !file.isEmpty()) {
                // Bild speichern, ApiProfilBildController.java
                boolean uploaded = apiProfilController.bildUpload(file, recipientId, "CHANNEL");
                if (uploaded) {
                    newChannel.setChannelPicture(recipientId);
                }
            }

            // D. In MongoDB Collection 'channels' speichern
            channelRepository.save(newChannel);

            // E. Kanal-ID zu den friendIds des Erstellers hinzufügen
            Optional<User> meOpt = userRepository.findById(meId);
            if (meOpt.isPresent()) {
                User ich = meOpt.get();
                if (!ich.getFriendIds().contains(recipientId)) {
                    ich.getFriendIds().add(recipientId);
                    userRepository.save(ich);
                }
                return ResponseEntity.ok("Kanal erfolgreich angelegt!");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User nicht gefunden.");

        } catch (Exception e) {
            System.err.println("Fehler beim Erstellen des Kanals: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Serverfehler.");
        }
    }

    /**
     * Daten von einem Channel Laden für den HiobsClient/ChannelEditController/updateChannel
     *
     * @param channelId
     * @return
     */
    @PostMapping(path = "/channel/channelData")
    public ResponseEntity<List<Channel>> channelData(
            @RequestHeader(value = "ownerID", required = false) String channelId) {

        // 1. Bereinigen der gesendeten ID (entfernt evtl. Anführungszeichen oder Leerzeichen)
        String cleanChannelId = (channelId != null) ? channelId.replace("\"", "").trim() : "";

        if (cleanChannelId.isEmpty()) {
            //System.err.println("Abbruch: Keine gültige Owner-ID übergeben.");
            return new ResponseEntity<>(List.of(), HttpStatus.BAD_REQUEST);
        }

        try {
            // 2. Alle Kanäle aus der Collection 'channels' über das Repository suchen
            List<Channel> aChannel = channelRepository.findByServerId(cleanChannelId);

            //System.out.println("Gefundene Kanal: " + aChannel);

            // 3. Trefferliste an den HiobsClient zurückgeben
            return new ResponseEntity<>(aChannel, HttpStatus.OK);

        } catch (Exception e) {
            //System.err.println("Fehler beim Abrufen der Kanäle: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Update Channel: Bild, Name oder Beschreibung ändern
     * @return
     */
    @PostMapping(path = "/channel(channelUpdate")
    public ResponseEntity<List<Channel>> updateChannel(@RequestBody Channel newChannel) {

        System.out.println("Kanal: " + newChannel);
        return  null;
    }


    /**
     * Kanal Löschen
     */
    @PostMapping(path = "/channel/channelDelete")
    public ResponseEntity<String> deleteChannel(
            @RequestParam("me") String meId,
            @RequestParam("channel") String channel) {

        // 1. Kanal aus meinem Array entfernen
        Update updateMe = new Update().pull("friendIds", channel);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(channel)), updateMe, User.class);

        return ResponseEntity.ok("Kanal gelöscht");
    }
}
