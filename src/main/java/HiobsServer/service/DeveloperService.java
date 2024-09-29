package HiobsServer.service;

import HiobsServer.model.Developer;
import HiobsServer.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Den 15.09.2024
 */

@Service
public class DeveloperService implements UserDetailsService {

    @Autowired
    private DeveloperRepository developerRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Developer users = developerRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Name oder Email existiert nicht"));

        //System.out.println("User service: " + users);

        return new MyDeveloperDetails(users);
    }
}
