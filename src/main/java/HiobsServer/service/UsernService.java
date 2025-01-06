package HiobsServer.service;

import HiobsServer.model.Usern;
import HiobsServer.repository.UsernRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Den 21.11.2024
 */

@Service
public class UsernService {

    @Autowired
    private UsernRepository usernRepository;


    /**
     * BENUTZT: ApiLoginController
     * <br><br>
     *
     * @param usern
     * @return
     */
    public Usern userSave(Usern usern) {
        return usernRepository.save(usern);
    }


    /**
     * BENUTZT: von ApiSperreController
     * <br><br>
     * Sperre aufheben, wenn gesperrt werden....
     * Parameter:   sperrdata -> null (soll zugesendet sein)
     *              token -> user token
     *  <br>
     * @param sperrdata
     * @param token
     * @return
     */
    public Integer sperreUpdate(String sperrdata, String token) {

        return usernRepository.updateSperre(sperrdata, token);
    }


    /**
     * BENUTZT: ApiLoginController
     * <br><br>
     * registrierte user mit diese e-mail suchen
     * @param mail
     * @return
     */
    public Usern userSuchen(String mail) {

        return usernRepository.findByEmail(mail);
    }


    /**
     * BENUTZT: von?
     * <br><br>
     * @param token
     * @return
     */
    public String tokenSuchen(String token) {

        return usernRepository.findByToken(token).getToken();
    }


    /**
     * BENUTZT: von ApiSperreController
     * <br><br>
     *
     * result: null oder millis(1234567890)
     *
     * @param token
     * @return
     */
    public Long sperrePrufen(String token) {

        return usernRepository.findByToken(token).getSperrdatum();

    }

}
