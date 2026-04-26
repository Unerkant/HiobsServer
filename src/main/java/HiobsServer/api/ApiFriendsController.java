package HiobsServer.api;

import HiobsServer.model.User;
import HiobsServer.repository.UserRepository;
import HiobsServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Den 18.05.2025
 */

@RestController // RestController schickt automatisch JSON zurück
public class ApiFriendsController {

    @Autowired
    private UserService userService; // Dieser Service greift auf das UserRepository zu
    @Autowired
    private UserRepository userRepository;

    /**
     * Lädt alle Profile der Freunde für einen bestimmten User.
     * Aufruf: GET /allFriends/all?myId=...
     */
    @PostMapping(path = "/allFriends/all")
    public ResponseEntity<List<User>> allfriends(@RequestParam("myId") String myId) {

        // 1. Wir holen den User aus MongoDB
        User me = userService.getUserById(myId);

        if (me == null || me.getFriendIds() == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        // 2. Wir laden alle User-Profile, deren IDs in meiner 'friendIds' Liste stehen
        // In MongoDB enthält 'friendIds' die Strings der Partner
        List<User> friends = userService.getUsersByIds(me.getFriendIds());
        return ResponseEntity.ok(friends);
    }

    /**
     * Lädt ein einzelnes User-Profil (für den Chat-Header).
     * Aufruf: GET /oneFriends/{recipientId}
     */
    @PostMapping(path = "/oneFriends/{recipientId}")
    public ResponseEntity<User> onefriends(@PathVariable("recipientId") String recipientId) {

        // Sonderfall für System-IDs (damit der Server keinen Fehler wirft)
        if (recipientId.equals("system_hiobs") || recipientId.equals("self_storage")) {
            return ResponseEntity.notFound().build();
        }

        User user = userService.getUserById(recipientId);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * Freunde Einladen
     */
    @PostMapping("/friends/add")
    public ResponseEntity<String> addFriend(@RequestParam("me") String myId,
                                            @RequestParam("friend") String friendId) {

        //System.out.println("Adding friend(ApiFriendsController -> Zeile: 71) " + friendId + " to " + myId);

        // 1. Beide User aus der DB laden
        Optional<User> meOpt = userRepository.findById(myId);
        Optional<User> friendOpt = userRepository.findById(friendId);

        if (meOpt.isPresent() && friendOpt.isPresent()) {
            User me = meOpt.get();
            User friend = friendOpt.get();

            // 2. Freund-IDs hinzufügen (falls noch nicht vorhanden)
            if (!me.getFriendIds().contains(friendId)) {
                me.getFriendIds().add(friendId);
                userRepository.save(me);
            }

            // Optional: Automatisch auch beim Freund hinzufügen (beidseitig)
            if (!friend.getFriendIds().contains(myId)) {
                friend.getFriendIds().add(myId);
                userRepository.save(friend);
            }

            return ResponseEntity.ok("Freundschaft erfolgreich gespeichert");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User nicht gefunden");
    }
}
