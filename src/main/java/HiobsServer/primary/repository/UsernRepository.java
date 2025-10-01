package HiobsServer.primary.repository;

import HiobsServer.primary.model.Usern;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 20.11.2024
 */

@Repository
public interface UsernRepository extends CrudRepository<Usern, Integer> {



    /**
     * BENUTZT:     1. von ApiMailController
     *
     * @param email
     * @return
     */
    Usern findByEmail(String email);


    /**
     * BENUTZT: ???
     * <br><br>
     *
     * @param token
     * @return
     */
    Usern findByToken(String token);

}
