package HiobsServer.service;

import HiobsServer.primary.model.Friends;
import HiobsServer.primary.repository.FriendsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Den 18.05.2025
 */

@Service
public class FriendsService {


    @Autowired
    private FriendsRepository friendsRepository;


    /**
     * Alle meine Freunde laden
     *
     * @return  null oder array
     */
    public Friends allFreunde() {

        Iterable<Friends> result = friendsRepository.findAll();
        if (!result.iterator().hasNext()) {
            return null;
        }
        return (Friends) result;
    }


    /**
     * finde Hiobs Post message Token
     *
     * @param meintoken
     * @return
     */
    public Iterable<Friends> findeMeineFreunde(String meintoken) {

        Iterable<Friends> result = friendsRepository.findByMeinentoken(meintoken);
        if(!result.iterator().hasNext()) {
            return null;
        }
        return result;
    }


    /**
     * Finde einer Freund mit Message Token
     *
     * @param msgtoken
     * @return
     */
    public Iterable<Friends> findeEinFreund( String msgtoken) {

        Iterable<Friends> result = friendsRepository.findByMessagetoken(msgtoken);
        if (!result.iterator().hasNext()) {
            return null;
        }
        return result;
    }

    /**
     * Neuer Freund speichern
     *
     * @param friends
     * @return
     */
    public Friends freundSave(Friends friends) {
       return friendsRepository.save(friends);
    }


  /*  Iterable<Token> result = tokenRepository.findAll();

        if (!result.iterator().hasNext()){
        return null;
    }
        return result.iterator().next().getMytoken();*/

}
