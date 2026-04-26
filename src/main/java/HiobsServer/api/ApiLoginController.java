package HiobsServer.api;

import HiobsServer.dto.LoginServerDaten;
import HiobsServer.dto.LoginClientSave;
import HiobsServer.controller.GlobaleException;
import HiobsServer.model.Sperre;
import HiobsServer.model.User;
import HiobsServer.service.LoginService;
import HiobsServer.service.SperreService;
import HiobsServer.service.UserService;
import HiobsServer.utilities.MailSenden;
import HiobsServer.utilities.MyUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Den 24.10.2024
 */

//@Controller
@RestController
public class ApiLoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private MyUtilities myUtilities;
    @Autowired
    private MailSenden mailSenden;
    @Autowired
    private LoginService loginService;
    @Autowired
    private GlobaleException globaleException;
    @Autowired
    private SperreService sperreService;


    /**
     * AktivierungCode an diese E-Mail versenden
     * return: an HiobsClient/MailController →  @PostMapping(value = "/login/mail")
     *
     * return sperrZeit, wenn vorhanden ist
     *      return versendet über den userReturn.setUsermail("nichtversendet");
     * return: E-Mail, wenn vorhanden ist
     * return: String 'nichtgefunden', keine E-Mail in Datenbenk vorhanden
     * return: String 'nichtversendet', kein anmeldeCode versendet
     */
    @PostMapping(value = "/loginMail")
    public ResponseEntity<User> loginmail(@RequestBody User loginObject) {

        // 1. nach User Daten in Datenbank suchen
        User userReturn = new User();
        User userDaten = userService.findeUser(loginObject.getUsermail());

        // 2. User Sperre Prüfen, wenn vorhanden: return
        if (userDaten != null && userDaten.getSperrdatum() != null) {

            // Sperre in Datenbank H2 Speichern, für Globale abruff
            sperreService.sperreAktivieren(sperreCreated(userDaten));

            userReturn.setSperrdatum(userDaten.getSperrdatum());
            return ResponseEntity.ok(userReturn);   // return: sperrDatum in Millis (1234567890)
        }


        // 3. AnmeldeCode holen und versenden an zugesendete E-Mail
        int aktuelleAnmeldeCode = myUtilities.aktivierungsCode();
        String output = mailSenden.sendEmail(loginObject.getUsermail(), aktuelleAnmeldeCode);

        // 5. output: versendet/nichtversendet, von utilities/MailSenden
        if (output.equals("versendet")) {

            // Record anmelde Daten für loginSave zum vergleichen, in LoginService(MAP) speichern
            String existsMail = userDaten == null ? "nichtgefunden" : userDaten.getUsermail();
            LoginServerDaten serverDaten =
                    new LoginServerDaten(loginObject.getUsermail(), existsMail, aktuelleAnmeldeCode );
            loginService.save(serverDaten);

            System.out.println("ApiLoginController/loginMail, Zeile:75, mit e-mail versendet:  " + serverDaten.anmeldeCode());

            //return:
            userReturn.setUsermail(existsMail);
            return ResponseEntity.ok(userReturn);     //return: nichtgefunden oder E-Mail

        } else {

            // Fehler in Globalen Exception speichern, output: nichtversendet
            loginFehler(output, loginObject.getUsermail());

            //Sicherheit code per E-Mail wurde nicht versendet, return output (Text: 'nichtversendet')
            userReturn.setUsermail("nichtversendet");
            return ResponseEntity.ok(userReturn);   // return: nichtversendet
        }

    }


    /**
     *  Daten aus den MongoDB, collection=users
     *  login Save, newUser: Optional[User{id='6952e6d4e1243e7de37a2bb8', username='example',
     *  usermail='example@example.com', password='default_start_password', roles=[USER], friendIds=[],
     *  createdAt=2025-12-29T21:38:44.842, active=true, profilePicture='null'}]
     */
    @PostMapping(value = "/loginSave")
    public ResponseEntity<?> loginsave(@RequestBody LoginClientSave clientSave) {

        try {
            // 1. Sicherheits-Check, anmelde Code prüfen
            LoginServerDaten storageDaten = loginService.get(clientSave.userMail());
            if (storageDaten != null && storageDaten.anmeldeCode() != clientSave.codeVonMail()) {

                return ResponseEntity.ok("falschecode");
            }

            // 2. User nach E-Mail in Datenbank suchen
            User existingUser = userService.findeUser(clientSave.userMail());
            User userToReturn = null, userCreated;
            if (existingUser != null) {
                //System.out.println("alt User: " + existingUser);
                userToReturn = existingUser;
            } else {

                // Neuer User anlegen
                userCreated = createNewUser(clientSave.userMail());
                userToReturn = userService.userSave(userCreated);
            }

            // Erfolg: Wir senden das komplette User-Objekt zurück
            return ResponseEntity.status(HttpStatus.OK).body(userToReturn);

        } catch (Exception e) {
            // Falls die MongoDB nicht erreichbar ist oder ein anderer Fehler auftritt
            loginFehler("noSave", e.getMessage());
            return ResponseEntity.ok("noSave");
        }
    }


    /**
     * new User Created
     *
     * @param email
     * @return
     */
    private User createNewUser(String email) {

        User newUser = new User();
        newUser.setDatum(Instant.now());
        newUser.setUsermail(email);
        newUser.setPseudonym(email.substring(0, 2).toUpperCase());
        newUser.setPassword("default_start_password");
        newUser.getRoles().add("USER");

        // Wir fügen die Standard-Kanäle direkt zur Liste hinzu
        // "system_hiobs" = Der globale News-Kanal
        // "self_storage" = Die persönliche Cloud
        newUser.setFriendIds(new ArrayList<>(List.of("system_hiobs", "self_storage")));

        return newUser;
    }



    /**
     * Sperre Created für Datenbank eintrag
     * @param user
     * @return
     */
    private Sperre sperreCreated(User user) {
        Sperre sperre = new Sperre();
        sperre.setSperrdatum(user.getSperrdatum());
        sperre.setToken(user.getServerId());

        return sperre;
    }



    /**
     * hier werden die Fehler von diese Login an GlobaleException gesendet
     */
    public void loginFehler(String aktion, String param) {

        int fehlersCode         = 0;
        String fehlersQuelle    = "";
        String fehlersText      = "";

        switch (aktion) {
            case "nichtversendet":      fehlersCode     = 405;
                                        fehlersQuelle   = "HiobsServer/ApiLoginControlle/@PostMapping" +
                                                                    "(value = \"/loginMail\")";
                                        fehlersText     = "E-Mail mit anmelde Code an ["+ param +"] nicht " +
                                                                    "verschickt, utilities/MailSenden/SendEmail  ";
                                        break;
            case "noSave":              fehlersCode     = 133;  //MongoDb-> Command not Found
                                        fehlersQuelle   = "HiobsServer/ApiLoginController/loginSave";
                                        fehlersText     = "Es ist der Exception Fehelr: " + param;
                                        break;
            default: break;
        }

        // Fehler ins Datenbank speichern HiobsServer H2 exception
        globaleException.exceptionsMelden(fehlersCode, fehlersQuelle, fehlersText);
    }

}
