package HiobsServer.repository;

import HiobsServer.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Den 15.09.2024
 */

@Repository
public interface RolesRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findByAuthority(String authority);
}
