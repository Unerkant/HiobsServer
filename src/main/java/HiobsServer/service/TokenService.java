package HiobsServer.service;

import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Den 1.11.2024
 */

@Service
public class TokenService {

    /**
     * Neue User Token generieren
     * wird generiert von aktuelle Datum als 14-stellige Zahlen
     * Tag+Monat+Jahr+Stunde+Minute+Sekunde
     *
     * @return
     */
    public String IdentifikationToken(){
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date token = new Date();
        return format.format(token);
    }
}
