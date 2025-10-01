package HiobsServer.api;

import HiobsServer.primary.model.Friends;
import HiobsServer.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Den 18.05.2025
 */

@Controller
public class ApiFriendsController {

    @Autowired
    private FriendsService friendsService;

    /**
     * Alle Freunde Laden
     * Benutzt: http://localhost:8090/msg, HiobsClient/MsgController Zeile: ca. 74
     *
     * @param mytoken
     * @return
     */
    @PostMapping(path = "/allFriends")
    public ResponseEntity<Iterable<Friends>> allfriends(@RequestBody String mytoken) {

        // Alle Freunde aus der Datenbank laden & an HiobsClient/MsgController senden, return null oder ArrayList
        Iterable<Friends> meineFreunde = friendsService.findeMeineFreunde(mytoken);
        return ResponseEntity.status(HttpStatus.OK).body(meineFreunde);
    }


    /**
     * Einer Freund Finden, Benutzt: http://localhost:8090/msg, HiobsClent/MsgController Zeile: ca. 120
     * @param msgToken
     * @return
     */
    @PostMapping(path = "/oneFriend")
    public ResponseEntity<Iterable<Friends>> onefriend(@RequestBody String msgToken) {

        // Einer Freund finden mit Message Token und an HiobsClient/MsgController senden
        Iterable<Friends> einFreund = friendsService.findeEinFreund(msgToken);
        return ResponseEntity.status(HttpStatus.OK).body(einFreund);
    }


}
