package HiobsServer.repository;

import HiobsServer.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Den 19.12.2025
 * umstieg auf Database: MongoDB
 */

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Prüfen, ob ein Username schon vergeben ist
    boolean existsByUsername(String username);

    // Die Methodennamen bleiben EXAKT gleich!
    Optional<User> findByUsername(String username);

    // User nach E-Mail suchen
    User findByUsermail(String email);

    // MongoDB-spezifische Features (Beispiel: Suche in Listen)
    // wenn User eine Liste von Rollen haben:
    List<User> findByRolesContaining(String role);
}
