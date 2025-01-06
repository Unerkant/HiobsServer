package HiobsServer.repository;

import HiobsServer.model.Usern;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 20.11.2024
 */

@Repository
public interface UsernRepository extends CrudRepository<Usern, Integer> {


    /**
     * BENUTZT: von ApiSperreController
     * <br><br>
     * Update die spalte 'sperrdatum', setzen auf null bei sperre aufhebung
     * RESPONSE: 1 → bei ausgefühlte update
     *           0 → token nicht gefunden
     *
     * @param sperrdate
     * @param tok
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE USERN SET sperrdatum = :sperrdate WHERE token = :tok", nativeQuery = true)
    Integer updateSperre( String sperrdate, String tok );


    /**
     * BENUTZT:     1. von ApiMailController
     *
     * @param email
     * @return
     */
    Usern findByEmail(String email);


    /**
     * BENUTZT: von ApiSperreController
     * <br><br>
     *
     * @param token
     * @return
     */
    Usern findByToken(String token);
}
