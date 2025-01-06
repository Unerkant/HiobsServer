package HiobsServer.service;

import HiobsServer.model.Admin;
import HiobsServer.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Den 30.10.2024
 */

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * ACHTUNG: die Methode loadUserBaUsername(){} NICHT VERÄNDERN, wirt für Login(Security spring boot) benutzt
     * wird nach username oder email datenbank durchgesucht und in eine Liste gespeichert
     * Admin: [Admin{  id=1, datum='30.10.2024 15:10:15', username='Admin', uservorname='null',
     * pseudonym='null', email='admin@admin.de', telefon='null',
     * passwort='$2a$12$9L1myAJRx6/LUKcfmaJ3o.Bi64DC6J0.ZTvvOVfdlzox6ODabC7M.', other='null', role='ROLE_ADMIN'}]
     * die Daten werden an AdminDetails weiter geleitet zum weiteren verarbeitung( getAuthorities() )
     *
     * @param usernameOrEmal
     * @return
     * @throws UsernameNotFoundException
     *
     * ACHTUNG: Daten für den login, NICHT VERÄNDERN
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmal) throws UsernameNotFoundException {

        Admin admins =  adminRepository.findByUsernameOrEmail(usernameOrEmal, usernameOrEmal)
                .orElseThrow(() -> new UsernameNotFoundException("User Name oder Email existiert nicht"));
        return new AdminDetails(admins);
    }
}
