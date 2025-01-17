package HiobsServer.exception;

import HiobsServer.model.Exception;
import HiobsServer.service.ExceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Den 19.11.2024
 */

@Controller
public class GlobaleException {

    @Autowired
    private ExceptionService exceptionService;

    /**
     * ACHTUNG: Alle ankommende Fehler von anderer Server(HiobsClient, HiobsFX, ...)
     * <br><br>
     *
     *  die Nötige Daten für den Fehler ins Datenbank zu speichern
     *  <code>
     *     // von anderer Project mit Request gesendet
     *             String fehlerLink = webConfig.FILE_HTTP+"exception";     // http://localhost:8080/exception
     *             Exception except = new Exception();                      // exception json-array
     *             except.setDatum(myUtilities.deDatum());                  // set Daten
     *             except.setErrquelle("HiobsClient/MailController");
     *             ..... weiter spalten .....
     *             JSONObject object = new JSONObject(except);              // in Json Object umwandeln
     *             // Senden + Response
     *             HttpResponse<String> response = apiService.requestApi(fehlerLink, object.toString());
     *       GESENDET: wird gesendet als JSONObject:
     *       {"datum":"18-12-2024 21:14:52","errcode":200,"count":0,"errip":"192.168.0.246",
     *       "errtext":"Den Fehler kommt von response -> HiobsClient/MailController/ApiService.requestApi",
     *       "errquelle":"HiobsClient/MailController"}
     *  </code>
     *          ACHTUNG: Gesendet als JSONObject und empfangen als Exception-Array
     * <br><br>
     * 1. hier angekommenen JSONObject als Exception empfangen
     *      Exceptions{  id=null, count=0, datum='18-12-2024 17:55:10', errip='192.168.0.246', errcode=200,
     *      errquelle='HiobsClient/MailController', errtext='Den Fehler kommt von response →
     *      HiobsClient/MailController/ApiService.requestApi', other='null', role='null'}
     *
     * 2. von mehreren speicherung den gleichen Fehler wird zuerst nach statusCode verglichen
     * 3. bei gleichem statusCode wird nur count erhöht (nur für statistik)
     *
     * FAZIT: zugesendete daten können als Exception array gleich in Datenbank speichern
     *  1. prüfen in Datenbank, ob statusCode vorhanden ist
     *  2. bei nicht vorhandene statusCode den 'fehler' ins Datenbank speichern
     *  3. wenn statusCode vorhanden ist, nur 'count' update (um 1 erhöhen)
     *
     * @param fehler
     * @return
     */
    @PostMapping("/exception")
    public ResponseEntity<String> setApiFehler(@RequestBody Exception fehler) {

        Exception findeFehler = exceptionService.findeStatusCode(fehler.getErrcode());

        System.out.println("Exception: " + fehler);

        if (findeFehler == null) {
            // neue Fehler in Datenbank speichern
            exceptionService.saved(fehler);
        } else {
            int alterCode   = findeFehler.getErrcode();
            int alterCount  = findeFehler.getCount();
            alterCount = alterCount + 1;

            // wenn Fehler schon vorhanden ist, count Update(erhöhen) nach ip-Adresse
            exceptionService.countUpdateNachCode(alterCount, alterCode);
        }

        //return ResponseEntity.status(HttpStatus.OK).body("OK");
        return ResponseEntity.status(HttpStatus.OK).body("gespeichert");
    }


    /**
     * Alle ankommende Fehler nur von diesem Server*
     *  <br><br>
     *
     *  * hier werden alle Fehler nur von diesem Server ins datenbank eingetragen,
     *      weil wir brauchen keine request verbindung
     *
     */
    public void setInternFehler(Exception except) {

        Exception findFehler = exceptionService.findeStatusCode(except.getErrcode());

        if (findFehler == null) {
            // Neuer Fehler ins Datenbank speichern
            exceptionService.saved(except);
        } else {
            int altCode     = findFehler.getErrcode();
            int altCount    = findFehler.getCount();
            altCount        = altCount + 1;

            // wenn Fehler schon vorhanden ist, count um 1 erhöhen nach StatusCode
            exceptionService.countUpdateNachCode(altCount, altCode);
        }

    }

}
