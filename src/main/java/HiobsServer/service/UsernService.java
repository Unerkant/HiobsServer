package HiobsServer.service;

import HiobsServer.primary.model.Usern;
import HiobsServer.primary.repository.UsernRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
