package HiobsServer.api;

import HiobsServer.primary.model.Letztelogin;
import HiobsServer.service.LetzteloginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Den 5.04.2025
 */

@Controller
public class ApiLetzteLoginController {

    @Autowired
    private LetzteloginService letzteloginService;


    /**
     * DATEN PER REQUEST HOLEN
     * alle Anmeldungen von Datenbank holen, voraussetzung: user token
     *
     * ZUGESENDET: als String, token → 17012025112844
     * RETURN: aus Datenbank array als LetzteLogin(Model)
     * {"id":603,"appname":"HiobsClient","appvers":"0.0.5","browser":"Safari",
     *                      "datum":"16-04-2025 22:32:26","ip":"95.223.108.40",...}
     *  return an die adresse: HiobsClient/MsgController/ Zeile: ~60
     *  apiService.requestApi(letzteLoginPath, authService.authToken());
     *
     * @param token
     */
    @PostMapping(path = "/letzteLogin")
    public ResponseEntity<Letztelogin> letzteLogin(@RequestBody String token) {

        Letztelogin allEinloggen = letzteloginService.allEinloggen(token);
        return ResponseEntity.status(HttpStatus.OK).body(allEinloggen);
    }


    /**
     * DATEN PER REQUEST ERHALTEN
     * Neue Anmeldung/Einloggen ins Datenbank speichern
     * ZUGESENDET: von HiobsClient/MailLoginController Zeile: ~250
     * private void anmeldeProtokollieren(String token, String pseudonym, String browser, String platform, String action )
     *
     * return ist erforderlich, weil REQUEST von HiobsClient/ApiService benötigt eine ResponseEntity<String>
     *
     * @param loginDaten
     */
    @PostMapping(path = "/letzteLoginSave")
    public ResponseEntity<Long> letzteLoginSave(@RequestBody Letztelogin loginDaten) {

        Letztelogin responseLogin = letzteloginService.loginSave(loginDaten);
        // symbolische response als ID, nicht erforderlich
        return ResponseEntity.status(HttpStatus.OK).body(responseLogin.getId());
    }


    /**
     * DATEN INTERN ERHALTEN(nur für HiobsServer)
     * Anmeldung vorgang in Datenbank speichern
     * ==============================================================
     * ACHTUNG: nur für internen bedarf, gesendet nur von hier, HiobsServer... nicht von
     * anderer APP, wie HiobsClient oder HiobsWeb, von außen werden die Daten per API
     * empfangen, hier gleich oben... @PostMapping(path = "/letzteLoginSave")
     *
     * BENUTZT: z.b.s. nach erfolgreichen versenden die sicherheit codes per E-Mail wird dann
     *                 eine nachricht an eine von dieser User registrierte APP(wenn solche gibst)
     *                 gesendet und gleich hier in Datenbank speichern, jeder zeit abrufbar unter
     *                 'Hiobs Post' in der Freuden raster
     * @param loginDaten
     */
    public void anmeldungSave(Letztelogin loginDaten) {

        Letztelogin output = letzteloginService.loginSave(loginDaten);

    }


}
