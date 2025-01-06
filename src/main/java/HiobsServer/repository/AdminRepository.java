package HiobsServer.repository;

import HiobsServer.model.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Den 30.10.2024
 */

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {

    // suchen nach Name oder E-Mail
    Optional<Admin> findByUsernameOrEmail(String username, String email);
}
