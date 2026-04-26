package HiobsServer.admin.repository;


import HiobsServer.admin.model.AdminLogin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminLoginRepository extends CrudRepository<AdminLogin, Integer> {
    //mach was
}
