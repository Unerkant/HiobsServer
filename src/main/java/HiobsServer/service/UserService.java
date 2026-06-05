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
     * BENUTZT: ApiLoginController Zeile: 134
     */
    public User userSave(User usern) {

        return userRepository.save(usern);
    }

    /**
     * User nach E-Mail suchen
     * BENUTZT: ApiLoginController Zeile: 60 & 125
     */
    public User findeUser(String usermail) {

        return userRepository.findByUsermail(usermail);
    }


    /**
     * BENUTZT: ApiLoginController
     * <br><br>
     * registrierte user mit diesem E-Mail suchen
     */
    public Optional<User> findUserName(User name) {

        return userRepository.findByUsername(String.valueOf(name));
    }


    /**
     * BENUTZT: zurzeit nicht
     */
    public List<User> datenSuchen(User roles) {

        return userRepository.findByRolesContaining(String.valueOf(roles));
    }

    /**
     * Benutzt: ApiFriendsController Zeile: 36 & 60
     */
    public User getUserById(String myId) {

        return userRepository.findById(myId).orElse(null);
    }

    /**
     *  Benutzt: ApiFriendsController Zeile: 44
     */
    public List<User> getUsersByIds(List<String> friendIds) {
        return userRepository.findAllById(friendIds);
    }
}
