package HiobsServer.service;

import HiobsServer.model.User;
import HiobsServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Den 21.11.2024
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean existingUser(User username) {

        return userRepository.existsByUsername(String.valueOf(username));
    }

    /**
     * BENUTZT: ApiLoginController
     * <br><br>
     *
     * @param usern
     * @return
     */
    public User userSave(User usern) {

        return userRepository.save(usern);
    }

    /**
     * User nach E-Mail suchen
     * @param usermail
     * @return
     */
    public User findeUser(String usermail) {

        return userRepository.findByUsermail(usermail);
    }


    /**
     * BENUTZT: ApiLoginController
     * <br><br>
     * registrierte user mit diese e-mail suchen
     * @param name
     * @return
     */
    public Optional<User> findUserName(User name) {

        return userRepository.findByUsername(String.valueOf(name));
    }


    /**
     * BENUTZT: von?
     * <br><br>
     * @param roles
     * @return
     */
    public List<User> datenSuchen(User roles) {

        return userRepository.findByRolesContaining(String.valueOf(roles));
    }

    public User getUserById(String myId) {
        return userRepository.findById(myId).orElse(null);
    }

    public List<User> getUsersByIds(List<String> friendIds) {
        return userRepository.findAllById(friendIds);
    }
}
