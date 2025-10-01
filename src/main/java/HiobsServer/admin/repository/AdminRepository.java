package HiobsServer.admin.repository;

import HiobsServer.admin.model.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Den 30.10.2024
 */

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {

    // suchen nach Name oder E-Mail
    Optional<Admin> findByUsernameOrEmailOrTelefon(String username, String email, String telefon);
}
