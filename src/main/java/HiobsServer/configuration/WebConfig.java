package HiobsServer.configuration;

import HiobsServer.utilities.MyUtilities;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


/**
 * Den 25.10.24
 */

@Configuration
public class WebConfig {


    public String FILE_HTTP;

    @Autowired
    private MyUtilities myUtilities;
    public WebConfig(MyUtilities myUtilities1){
        this.myUtilities = myUtilities1;
    }

    @PostConstruct
    public void init() {

        if (myUtilities.getHost().equals("localhost")) {

            FILE_HTTP = "http://localhost:8080/";
        } else {

            FILE_HTTP = "http://194.164.63.85:8080/";
        }
        //System.out.println("Config Init: " + myUtilities.getHost() +"/"+ myUtilities.getLocalhostIp());
    }


    public final String FILE_CSS                = "/static/css/style.css";
    public final String FILE_URL                = "???";
    public final String FILE_MYSQL              = "http://localhost:8889/";     /* mysql */
    /* Bild Adresse BEI BOTE:  http://localhost:8080/profilbild/03052022103644.png */
}
