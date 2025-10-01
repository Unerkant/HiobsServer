package HiobsServer.primary.repository;

import HiobsServer.primary.model.Letztelogin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Den 5.04.2025
 */

@Repository
public interface LetzteloginRepository extends CrudRepository<Letztelogin, Integer> {

    // Anmeldung nach Token suchen
    Letztelogin findByToken(String token);
}
