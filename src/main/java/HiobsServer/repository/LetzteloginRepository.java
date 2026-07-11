package HiobsServer.repository;

import HiobsServer.model.Letztelogin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 5.04.2025
 */

@Repository
public interface LetzteloginRepository extends MongoRepository<Letztelogin, String> {

    // Anmeldung nach Token suchen
    Letztelogin findByUsertoken(String token);
}
