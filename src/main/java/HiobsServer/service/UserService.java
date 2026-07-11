package HiobsServer.service;

import HiobsServer.model.User;
import HiobsServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Den 21.11.2024
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;


    /**
     *  Letzte Online Zeit Aktualisieren
     *  benutzt: MessageController
     */
    public void updateLastLogin(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setLastLogin(Instant.now());
            userRepository.save(user);
        }
    }


    /**
     * Benutzt: unbekannt
     */
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
     * BENUTZT: ApiLoginController Zeile: 60 & 125
     * <br><br>
     *  User nach E-Mail suchen
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
     * Benutzt: ApiFriendsController Zeile: 45 & 75
     */
    public User getUserById(String myId) {

        return userRepository.findById(myId).orElse(null);
    }


    /**
     *  Benutzt: ApiFriendsController Zeile: 56
     */
    public List<User> getUsersByIds(List<String> friendIds, String myId) {

        List<User> freunde = userRepository.findAllById(friendIds);

        // Füge System-User NUR HINZU, wenn sie in den friendIds vorkommen
        // und nicht bereits durch findAllById geladen wurden.
        if (friendIds.contains("system_hiobs")) {
            freunde.add(createSystemUser("system_hiobs", "Hiobs", "HP", "system_hiobs", "Servicemeldungen"));
        }
        if (friendIds.contains("self_storage")) {
            freunde.add(createSystemUser("self_storage", "Gespeichertes", "GS", "self_storage", "Nur für dich"));
        }

        // setzt letzte Nachrichten in List<User> rein
        messageService.setzeLetzteNachrichten(freunde, myId);

        // Sortierung nach DatumLetzteNachricht absteigend (die neuesten zuerst)
        freunde.sort((u1, u2) -> {
            // Falls keine Nachricht vorhanden ist, nach hinten sortieren (nullsLast)
            if (u1.getDatumLetzteNachricht() == null && u2.getDatumLetzteNachricht() == null) return 0;
            if (u1.getDatumLetzteNachricht() == null) return 1;
            if (u2.getDatumLetzteNachricht() == null) return -1;

            // Vergleiche die Instants (Datum)
            return u2.getDatumLetzteNachricht().compareTo(u1.getDatumLetzteNachricht());
        });

        return freunde;
    }


    // Hilfsmethode, um ein persönliches User-Objekt für die Anzeige zu bauen
    private User createSystemUser(String id, String username, String pseudonym, String picture, String roles) {
        User us = new User();
        us.setServerId(id);
        us.setUsername(username);
        us.setPseudonym(pseudonym);
        us.setProfilePicture(picture);
        us.setRoles(Collections.singletonList(roles));
        // Wichtig: Hier keine IDs setzen, die MongoDB nicht kennt
        return us;
    }

}
