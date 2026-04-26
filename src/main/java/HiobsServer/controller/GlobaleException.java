package HiobsServer.controller;

import HiobsServer.admin.model.Exception;
import HiobsServer.admin.service.ExceptionService;
import HiobsServer.utilities.GeoLocation;
import HiobsServer.utilities.MyUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Den 19.11.2024
 */

/**
 * ACHTUNG: SERVICE + REPOSITORY IN ADMIN BEREICH
 */

@RestController
public class GlobaleException {

    @Autowired
    private ExceptionService exceptionService;
    public GlobaleException(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @Autowired
    private MyUtilities myUtilities;
    @Autowired
    private GeoLocation geoLocation;


    /**
     * ACHTUNG: Alle ankommende Fehler von anderer Server(HiobsClient, HiobsFX, ...)
     * <br><br>
     * BESCHREIBUNG: zugesendete daten können als Exception array gleich in Datenbank speichern
     *  1. prüfen in Datenbank, ob statusCode vorhanden ist
     *  2. bei nicht vorhandene statusCode den 'fehler' ins Datenbank speichern
     *  3. wenn statusCode vorhanden ist, nur 'count' update (um 1 erhöhen)
     */
    @PostMapping(value = "/exceptionen")
    public ResponseEntity<String> exceptionen(@RequestBody Exception exceptions) {

        exceptionSave(exceptions);

        //return ResponseEntity.status(HttpStatus.OK).body("OK");
        return ResponseEntity.status(HttpStatus.OK).body("gespeichert");
    }



    /**
     * Fehler von dieser Server + Admin hier zusammen stellen, weiter zu exceptionSave
     * ACHTUNG: soll zugesended: StatusCode + Fehler Quelle + Fehler Text
     */
    public void exceptionsMelden(int code, String quelle, String text) {

        Exception exception = new Exception();
        exception.setId(1L);
        exception.setCount(1);
        exception.setDatum(String.valueOf(myUtilities.deDatum()));
        exception.setErrip(geoLocation.clientIp());
        exception.setErrcode(code);
        exception.setErrquelle(quelle);
        exception.setErrtext(text);
        exception.setOther("");
        exception.setRole("");

        exceptionSave(exception);
    }


    /**
     * Alle ankommende Fehler in H2, adminHiobs/exception speichern
     */
    public void exceptionSave(Exception except) {

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
