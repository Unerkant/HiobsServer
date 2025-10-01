package HiobsServer.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;


/**
 * Den 25.10.24
 */

@Configuration
public class WebConfig {

    @PostConstruct
    public void init() {
        // mach was
    }


    public final String FILE_CSS                = "/static/css/style.css";
    public final String FILE_HTTP               = "http://localhost:8080/";
    //public final String FILE_HTTP             = "http://194.164.63.85:8080/";
    public final String FILE_URL                = "???";
    public final String FILE_MYSQL              = "http://localhost:8889/";     /* mysql */
    /* Bild Adresse BEI BOTE:  http://localhost:8080/profilbild/03052022103644.png */
}
