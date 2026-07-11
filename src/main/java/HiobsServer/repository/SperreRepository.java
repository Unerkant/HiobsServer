package HiobsServer.repository;

import HiobsServer.model.Sperre;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 16.01.2024, Update auf MongoDB von 16.01.2026
 */

@Repository
public interface SperreRepository extends MongoRepository<Sperre, String> {

    /**
     * finde alle gesperete Token
     * @param token
     * @return
     */
    Optional<Sperre> findByToken(String token);


    // Findet alle Sperren, deren Millisekunden-Wert kleiner als der aktuelle ist
    List<Sperre> findBySperrdatumLessThan(Long jetztMillis);


    /**
     * Sperre Update
     * @param sperrdateMillis
     * @param tok
     */
    @Query("{ 'token' : ?1 }")
    @Update("{ '$set' : { 'sperrdatum' : ?0 } }")
    void updateSperre(Long sperrdateMillis, String tok);

}

