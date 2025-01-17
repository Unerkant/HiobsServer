package HiobsServer.repository;

import HiobsServer.model.Sperre;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 16.01.2024
 */

@Repository
public interface SperreRepository extends CrudRepository<Sperre, Integer> {


    Sperre findByToken(String token);


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
    @Query(value = "UPDATE SPERRE SET sperrdatum = :sperrdate WHERE token = :tok", nativeQuery = true)
    Integer updateSperre( String sperrdate, String tok );
}
