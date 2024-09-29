package HiobsServer.repository;

import HiobsServer.model.Developer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Den 15.09.2024
 */

@Repository
public interface DeveloperRepository extends CrudRepository<Developer, Integer> {

    Optional<Developer> findByUsernameOrEmail(String username, String email);
}
