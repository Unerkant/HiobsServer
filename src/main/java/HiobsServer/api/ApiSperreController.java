package HiobsServer.api;

import HiobsServer.service.SperreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Den 18.11.2024
 */

@Controller
public class ApiSperreController {

    @Autowired
    private SperreService sperreService;

    /**
     * AUFGABE: in die Tabelle Sperre, spalte 'sperrdatum' auf null setzen, nach Ablauf die Sperre
     * <br><br>
     * RESULT: 1 → wenn spalte 'sperrdatum' geleert wird
     *         0 → wenn kein token gefunden wird
     * <br><br>
     * ZUGESENDET: von HiobsClient/SperreController/  @DeleteMapping("/sperreDelete"){.. Zeile: ~63}
     * zugesendete Daten: nur token (21122024123456) als String
     *
     * @param token
     * @return
     */
    @PostMapping(value = "/sperreDeleteApi")
    public ResponseEntity<String> apiSperre(@RequestBody String token) {

        // Sperr Daten aus dem Datenbank Löschen
        sperreService.sperreAufheben(token);

        // response an HiobsClient/SperreController/@DeleteMapping
        return ResponseEntity.status(HttpStatus.OK).body("aufgehoben");

    }

}
