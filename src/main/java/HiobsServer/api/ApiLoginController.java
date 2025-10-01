package HiobsServer.api;

import HiobsServer.exception.GlobaleException;
import HiobsServer.primary.model.Exception;
import HiobsServer.primary.model.Usern;
import HiobsServer.primary.model.Friends;
import HiobsServer.service.FriendsService;
import HiobsServer.service.UsernService;
import HiobsServer.utilities.GeoLocation;
import HiobsServer.utilities.MailSenden;
import HiobsServer.utilities.MyUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Den 24.10.2024
 */

@Controller
public class ApiLoginController {

    @Autowired
    private ApiLetzteLoginController apiLetzteLoginController;
    @Autowired
    private MyUtilities myUtilities;
    @Autowired
    private GeoLocation geoLocation;
    @Autowired
    private MailSenden mailSenden;
    @Autowired
    private UsernService usernService;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private GlobaleException globaleException;

    private int sicherheitsToken    = 0;


    /**
     * BENUTZT: HiobsClient/MailController/@PostMapping(value = "/login/mail")
     * <br><br>
     *
     *  output: 'versendet', E-Mail ist vesendet
     *          'nichtversendet', kein E-Mail versand
     *
     *  return: statusCode → 400 + 'nosend', fehler beim Versenden
     *  return: stausCode → 200 + 'gefunden' oder 'nichtgefunden',
     *
     * @param mailZugesendet
     * @return
     */
    @PostMapping(value = "/loginMail")
    public ResponseEntity<String>apiMail(@RequestBody String mailZugesendet) {

        /**
         * AktivierungCode an diese e-mail versenden
         * return: an HiobsClient/MailController ->  @PostMapping(value = "/login/mail")
         * return: 'versendet', als versendet
         * return: 'nichtversendet', zurzeit nicht benutzt
         */
        sicherheitsToken = myUtilities.aktivierungsCode();
        String text = "<p>hier erhalten Sie ihre Messenger Aktivierung Code </p>"
                +"<b>" + sicherheitsToken + "</b>"
                +"<p>Bitte beachten Sie, dass dieser Token nur dieser Sitzung g&#252;ltig ist. </p>"
                +"<p>mit Freundlichen Gr&#252;ßen</p>"
                +"<p>Ihr HiobsPost Team</p>";
        String subject = "HiobsPost Sicherheitstoken";


        String output = mailSenden.sendEmail(mailZugesendet, text, subject);

        System.out.println("Mail: " +mailZugesendet);
        System.out.println("Zeile: 68, ApiLoginController -> "+ sicherheitsToken );

        if (output.equals("versendet")) {

            /**
             * Sicherungscode erfolgreich versendet
             * <br><br>
             * return an HiobsClient/MailController ->  @PostMapping(value = "/login/mail")
             * return: 200 (statusCode wird erwartet)
             * Daten in Datenbank suchen, ob User schon registriert ist oder neuer
             * return: 'gefunden' + 'nichtgefunden', sind erforderlich
             *
             * ACHTUNG: kein NULL response, geht auf errors
             */
            Usern datenSuchen = usernService.userSuchen(mailZugesendet);
            String mailSuchen = datenSuchen == null ? "nichtgefunden" : "gefunden";
            return ResponseEntity.status(HttpStatus.OK).body(mailSuchen);
        } else {

            /**
             * Sicherheitcode wurde nicht versendet
             * return: 402 (return nicht erforderlich)
             */
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nosend");
        }

    }


    /**
     * Daten werden von HiobsClient/MailController/@PostMapping(value = "/login/registrieren") zugesendet
     * neuDaten: (als Usern)
     *      mail: bei Registrierung/Einloggen eingegebene E-Mail
     *      fund: 'gefunden' oder 'nichtgefunden', ob die E-Mail in Datenbank vorhanden ist
     *      code: sicherheit code, die von E-Mail zugesendet waren
     *
     * ZUGESENDETE JSON: Neu Daten: Usern:
     *                  {  id=0, bild='null', datum='null', email='richterpaul@freenet.de', other='1111',
     *                  passwort='null', pseudonym='null', role='nichtgefunden', sprache='null',
     *                  sperrdatum=null, telefon='null', token='null', username='null', uservorname='null'}
     *
     * @param neuDaten
     * @return
     */
    @PostMapping(value = "/loginSave")
    public ResponseEntity<Usern> apiSave(@RequestBody Usern neuDaten) {

        // Daten vorbereiten
        String mail     = neuDaten.getEmail();
        String fund     = neuDaten.getRole().trim();
        int userCode    = Integer.parseInt(neuDaten.getOther().trim());
        String pseu     = mail.substring(0, mail.length() - mail.length() + 2);
        String datum    = myUtilities.deDatum();
        String sprache  = geoLocation.clientCity("countryCode");
        String meintoken                = myUtilities.IdentifikationToken();
        String hiobsMsgToken            = myUtilities.messageToken();
        String gespeichertesMsgToken    = myUtilities.otherToken();
        Friends newHiobs = new Friends();
        Friends newGespeichertes = new Friends();

        // User-Daten von Datenbank holen, wenn vorhanden sind?
        Usern newUser = new Usern();
        Usern altUser = usernService.userSuchen(mail);



        /**
         * Falsche Code eingegeben,
         * <br><br>
         * return an HiobsClient/MailController/@PostMapping(value = "/login/anmelden")
         * nur 3 Parameter zurückgesendet
         */
        if (sicherheitsToken != userCode) {
            newUser.setEmail(mail);
            newUser.setOther("falschecode");
            newUser.setPseudonym(pseu);
            newUser.setRole(fund);
            newUser.setToken(meintoken);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(newUser);
        }



        /**
         * Neuer User Registrieren oder alte User-Daten abrufen
         */
        if (altUser != null) {

            //User vorhanden, nur Daten abrufen
            newUser = usernService.userSuchen(mail);
            return ResponseEntity.status(HttpStatus.OK).body(newUser);

        } else {

            /**
             * User nicht vorhanden, neue anlegen
             *
             * ACHTUNG: bei neu Registrierung werde gleich message token für hiobs + gespeichertes angelegt
             * die 2 chats werden in Datenbank als Freunde angelegt mit engine message token
             * der speicherort von meinen Freunden: secondary/model/Friends
             *
             * BEMERKUNG: die 2 chats, Hiobs Post & Gespeichertes sind als bestandteil voll integriert
             * und können nicht gelöscht werden
             */
            newUser.setBild("");
            newUser.setDatum(datum);
            newUser.setEmail(mail);
            newUser.setOther("");
            newUser.setPasswort("");
            newUser.setPseudonym(pseu);
            newUser.setRole("");
            newUser.setSprache(sprache);
            newUser.setTelefon("");
            newUser.setToken(meintoken);
            newUser.setUsername("");
            newUser.setUservorname("");

            Usern saveUser = usernService.userSave(newUser);

            // Hiobs Post als Freund anlegen
            newHiobs.setDatum(datum);
            newHiobs.setFriendsbild("hiobspost");
            newHiobs.setFriendsmail("");
            newHiobs.setFriendsname("Hiobs");
            newHiobs.setFriendspseudonym("HP");
            newHiobs.setFriendstelefon("");
            newHiobs.setFriendstoken("");
            newHiobs.setFriendsvorname("Post");
            newHiobs.setGespeichertestoken("");
            newHiobs.setHiobstoken("");
            newHiobs.setMeinentoken(meintoken);
            newHiobs.setMessagetoken(hiobsMsgToken);
            newHiobs.setRole("Servicemeldungen");

            Friends saveHiobs = friendsService.freundSave(newHiobs);

            // Gespeichertes als Freund anlegen
            newGespeichertes.setDatum(datum);
            newGespeichertes.setFriendsbild("gespeichertes");
            newGespeichertes.setFriendsmail("");
            newGespeichertes.setFriendsname("Gespeichertes");
            newGespeichertes.setFriendspseudonym("GS");
            newGespeichertes.setFriendstelefon("");
            newGespeichertes.setFriendstoken("");
            newGespeichertes.setFriendsvorname("");
            newGespeichertes.setGespeichertestoken("");
            newGespeichertes.setHiobstoken("");
            newGespeichertes.setMeinentoken(meintoken);
            newGespeichertes.setMessagetoken(gespeichertesMsgToken);
            newGespeichertes.setRole("Privatsammlungen");

            Friends saveGespeichertes = friendsService.freundSave(newGespeichertes);

                if (saveUser != null) {

                    // Neuer User angelegt, zurück an HiobsClient/MailLoginController Zeile: 156
                    return ResponseEntity.status(HttpStatus.OK).body(saveUser);

                } else {

                    // fehler melden
                    int fehlerCode = 1004;
                    String fehlerQuelle = "HiobsServer/ApiLoginController/ @PostMapping(value = \"/loginSave\")";
                    String fehlerText   = "Neuer User speicherung in Datenbank fehlgeschlagen";
                    fehlerMelden(fehlerCode, fehlerQuelle, fehlerText);

                    // Daten sind nicht gespeichert
                    newUser.setEmail("save@fehler.com");
                    newUser.setOther("nosave");
                    newUser.setRole(fund);
                    return ResponseEntity.status(HttpStatus.OK).body(newUser);
                }
        }

    }


    /**
     * Fehler in globalen Datenbank speichern
     *
     * BESCHREIBUNG: den Fehler wird an GlobaleException gesendet und da weiter verarbeiten
     * PARAMETER:  Exception-Array (statusCode erforderlich)
     */
    public void fehlerMelden(int code, String quelle, String text) {

        Exception exception = new Exception();
        exception.setCount(1);
        exception.setDatum(myUtilities.deDatum());
        exception.setErrip(geoLocation.clientIp());
        exception.setErrcode(code);
        exception.setErrquelle(quelle);
        exception.setErrtext(text);
        exception.setOther("");
        exception.setRole("");

        globaleException.setInternFehler(exception);
    }

}
