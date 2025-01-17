package HiobsServer.api;

import HiobsServer.exception.GlobaleException;
import HiobsServer.model.Exception;
import HiobsServer.model.Usern;
import HiobsServer.service.UsernService;
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
    private MyUtilities myUtilities;
    @Autowired
    private MailSenden mailSenden;
    @Autowired
    private UsernService usernService;
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

        System.out.println("Mail: " +mailZugesendet);
        String output = mailSenden.sendEmail(mailZugesendet, text, subject);

        System.out.println("Zeile: 68, ApiLoginController -> "+ sicherheitsToken );

        if (output.equals("versendet")) {

            /**
             * Sicherungscode versendet
             * <br><br>
             * return an HiobsClient/MailController ->  @PostMapping(value = "/login/mail")
             * return: 200 (statusCode wird erwartet)
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

        Usern newUser = new Usern();

        // Daten vorbereiten
        String mail     = neuDaten.getEmail();
        String fund     = neuDaten.getRole().trim();
        int userCode    = Integer.parseInt(neuDaten.getOther().trim());
        String pseu     = mail.substring(0, mail.length() - mail.length() + 2);
        String datum    = myUtilities.deDatum();
        String sprache  = myUtilities.getLanguage();
        String token    = myUtilities.IdentifikationToken();


        /**
         * Falsche Code eingegeben,
         * <br><br>
         * return an HiobsClient/MailController/@PostMapping(value = "/login/anmelden")
         * nur 3 Parameter zurückgesendet
         */
        if (sicherheitsToken != userCode) {
            newUser.setEmail(mail);
            newUser.setOther("falschecode");
            newUser.setRole(fund);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(newUser);
        }



        /**
         * Neuer User Registrieren oder alte User-Daten abrufen
         */
        Usern altUser = usernService.userSuchen(mail);
        if (altUser != null) {

            //User vorhanden, nur Daten abrufen
            newUser = usernService.userSuchen(mail);
            return ResponseEntity.status(HttpStatus.OK).body(newUser);

        } else {

            //User nicht vorhanden, neue anlegen
            newUser.setBild("");
            newUser.setDatum(datum);
            newUser.setEmail(mail);
            newUser.setOther("");
            newUser.setPasswort("");
            newUser.setPseudonym(pseu);
            newUser.setRole("");
            newUser.setSprache(sprache);
            newUser.setTelefon("");
            newUser.setToken(token);
            newUser.setUsername("");
            newUser.setUservorname("");

            Usern saveUser = usernService.userSave(newUser);

                if (saveUser != null) {

                    // Neuer User angelegt, zurück an HiobsClient/success.html
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
        exception.setErrip(myUtilities.getLocalhostIp());
        exception.setErrcode(code);
        exception.setErrquelle(quelle);
        exception.setErrtext(text);
        exception.setOther("");
        exception.setRole("");

        globaleException.setInternFehler(exception);
    }
}
