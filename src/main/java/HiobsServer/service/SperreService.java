package HiobsServer.service;

import HiobsServer.primary.repository.SperreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Den 16.01.2024
 */

@Service
public class SperreService {

    @Autowired
    private SperreRepository sperreRepository;


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

        // return usernRepository.findByToken(token).getSperrdatum();
        return  sperreRepository.findByToken(token).getSperrdatum();

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

        return sperreRepository.updateSperre(sperrdata, token);
    }

}
