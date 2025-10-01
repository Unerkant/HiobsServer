package HiobsServer.primary.repository;

import HiobsServer.primary.model.Friends;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 18.05.2025
 */

@Repository
public interface FriendsRepository extends CrudRepository<Friends, Integer> {

    /**
     *  Aller Freunde finden nach meinen Token
     *
     * @param token
     * @return
     */
    Iterable<Friends> findByMeinentoken(String token);


    /**
     * Einer Freund finden nach Message Token
     *
     * @param msgtoken
     * @return
     */
    Iterable<Friends> findByMessagetoken(String msgtoken);

    // mach was

}
