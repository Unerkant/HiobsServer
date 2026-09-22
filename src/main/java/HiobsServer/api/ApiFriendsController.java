package HiobsServer.api;

import HiobsServer.model.User;
import HiobsServer.repository.UserRepository;
import HiobsServer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private final MongoTemplate mongoTemplate;
    public ApiFriendsController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    /**
     * Lädt alle Profile der Freunde für einen bestimmten User.
     * Aufruf: GET: .uri(webConfig.SERVER_HTTP + "allFriends/all?myId=" + myId)
     * HiobsClient/MsgController/private List<User> getAlleKontakte(String myId)  Zeile: 130
     * zugesendete Format: myId:6989c6273217d0d4651d7e42
     */
    @PostMapping(path = "/allFriends/all")
    public ResponseEntity<List<User>> allfriends(@RequestParam("myId") String myId) {

        // 1. Wir holen den User aus MongoDB
        User me = userService.getUserById(myId);

        if (me == null || me.getFriendIds() == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        // me.getFriendIds: [system_hiobs, self_storage, 69962ec8360a87668ab19142, 20260816180118]

        // 2. Wir laden alle User-Profile, deren IDs in meiner 'friendIds' Liste stehen
        // in MongoDB enthält 'friendIds' die Strings der Partner
        List<User> friends = userService.getUsersByIds(me.getFriendIds(), myId);

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


    /**
     * Freund & mich von friendIds(array) entfernen, Chat Löschen
     */
    @PostMapping("/friends/friendRemove")
    public ResponseEntity<String> removeFriends(@RequestParam("me") String myId,
                                                @RequestParam("friend") String friendId) {
        // 1. Freund aus meinem Array entfernen
        Update updateMe = new Update().pull("friendIds", friendId);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(myId)), updateMe, User.class);

        // 2. Mich aus dem Array des Freundes entfernen
        Update updateFriend = new Update().pull("friendIds", myId);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(friendId)), updateFriend, User.class);

        return ResponseEntity.ok("Freundschaft beendet");
    }
}
