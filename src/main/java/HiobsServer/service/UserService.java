package HiobsServer.service;

import HiobsServer.model.Channel;
import HiobsServer.model.User;
import HiobsServer.repository.ChannelRepository;
import HiobsServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
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
    @Autowired
    private ChannelRepository channelRepository;

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
        if (friendIds == null || friendIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<User> ergebnisListe = new ArrayList<>();

        // 1. Statische System-User (falls vorhanden)
        if (friendIds.contains("system_hiobs")) {
            ergebnisListe.add(createSystemUser("system_hiobs", "Hiobs", "HP",
                   "Servicemeldungen", "system_hiobs", "USER"));
        }
        if (friendIds.contains("self_storage")) {
            ergebnisListe.add(createSystemUser("self_storage", "Gespeichertes", "GS",
                    "Nur für dich","self_storage", "USER"));
        }

        // 2. IDs filtern, die reine User-IDs sind (keine System-IDs)
        List<String> potentielleUserIds = friendIds.stream()
                .filter(id -> !id.equals("system_hiobs") && !id.equals("self_storage"))
                .toList();

        // 3. Echte User aus MongoDB laden
        List<User> echteUser = userRepository.findAllById(potentielleUserIds);
        ergebnisListe.addAll(echteUser);

        // 4. Welche IDs aus friendIds wurden NICHT als User gefunden? Das müssen Kanäle sein!
        List<String> gefundeneUserIds = echteUser.stream().map(User::getServerId).toList();
        List<String> channelIds = potentielleUserIds.stream()
                .filter(id -> !gefundeneUserIds.contains(id))
                .toList();

        // 5. Kanäle aus der Collection 'channels' laden und in ein User-Format umwandeln
        if (!channelIds.isEmpty()) {
            List<Channel> kanele = channelRepository.findAllById(channelIds);
            for (Channel ch : kanele) {
                ergebnisListe.add(convertChannelToUser(ch));
            }
        }

        // 6. Letzte Nachrichten ermitteln
        messageService.setzeLetzteNachrichten(ergebnisListe, myId);

        // 7. Sortieren nach Datum der letzten Nachricht (neueste zuerst)
        ergebnisListe.sort((u1, u2) -> {
            if (u1.getDatumLetzteNachricht() == null && u2.getDatumLetzteNachricht() == null) return 0;
            if (u1.getDatumLetzteNachricht() == null) return 1;
            if (u2.getDatumLetzteNachricht() == null) return -1;
            return u2.getDatumLetzteNachricht().compareTo(u1.getDatumLetzteNachricht());
        });

        return ergebnisListe;
    }

    // Hilfsmethode, um ein persönliches User-Objekt für die Anzeige zu bauen
    private User createSystemUser(String id, String username, String pseudonym, String subtitle, String picture, String roles) {
        User us = new User();
        us.setServerId(id);
        us.setUsername(username);
        us.setPseudonym(pseudonym);
        us.setUsermail(subtitle);
        us.setProfilePicture(picture);
        us.setRoles(Collections.singletonList(roles));
        // Wichtig: Hier keine IDs setzen, die MongoDB nicht kennt
        return us;
    }

    // 💡 Hilfsmethode: Wandelt ein Channel-Objekt in ein User-Objekt um, damit das Frontend es darstellen kann
    private User convertChannelToUser(Channel channel) {
        String name = channel.getChannelName();
        String erster = (name != null && !name.isEmpty()) ? name.substring(0, 1) : "";

        User channelAsUser = new User();
        channelAsUser.setServerId(channel.getServerId());
        channelAsUser.setUsername(channel.getChannelName());
        channelAsUser.setPseudonym(erster);
        channelAsUser.setUsermail(channel.getChannelText()); // Beschreibung wird z. B. als Subtitle genutzt
        channelAsUser.setDatum(channel.getDatum());
        channelAsUser.setProfilePicture(channel.getChannelPicture());

        // Kennzeichnung als KANAL (wichtig für Frontend z.B. für Schreibrechte oder Icons)
        channelAsUser.setType("CHANNEL");
        channelAsUser.getRoles().add("CHANNEL");

        return channelAsUser;
    }
}
