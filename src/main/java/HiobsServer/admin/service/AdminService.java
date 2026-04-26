package HiobsServer.admin.service;

import HiobsServer.admin.model.Admin;
import HiobsServer.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Den 30.10.2024
 */

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminLoginService adminLoginService;

    public AdminService(AdminRepository adminRepo, AdminLoginService adminLoginService) {
        this.adminRepository = adminRepo;
        this.adminLoginService = adminLoginService;
    }
    int versuchCount = 0;

    /**
     * ACHTUNG: die Methode loadUserBaUsername(){} NICHT VERÄNDERN, wirt für Login(Security spring boot) benutzt
     * wird nach username oder email datenbank durchgesucht und in eine Liste gespeichert
     * Admin: [Admin{  id=1, datum='30.10.2024 15:10:15', username='Admin', uservorname='null',
     * pseudonym='null', email='admin@admin.de', telefon='null',
     * passwort='$2a$12$9L1myAJRx6/LUKcfmaJ3o.Bi64DC6J0.ZTvvOVfdlzox6ODabC7M.', other='null', role='ROLE_ADMIN'}]
     * die Daten werden an AdminDetails weiter geleitet zum weiteren verarbeitung( getAuthorities() )
     *
     * @param nameOrEmalOrTelefon
     * @return
     * @throws UsernameNotFoundException
     *
     * ACHTUNG: Daten für den login, NICHT VERÄNDERN
     * Suche nach Namen oder Mail
     */
    @Override
    public UserDetails loadUserByUsername(String nameOrEmalOrTelefon) throws UsernameNotFoundException {

       /* Admin admins = adminRepo.findByUsernameOrEmailOrTelefon(nameOrEmalOrTelefon, nameOrEmalOrTelefon, nameOrEmalOrTelefon)
                .orElseThrow(() -> new UsernameNotFoundException("User Name, Email oder Telefon existiert nicht"));
        return new AdminDetails(admins);*/
        Optional<Admin> admins = adminRepository.findByUsernameOrEmailOrTelefon(nameOrEmalOrTelefon, nameOrEmalOrTelefon, nameOrEmalOrTelefon);

        System.out.println("Admin Service, User Name: " + admins);

        // Anmeldung mit den falschen Namen ins Datenbank speichern, nach 5 versuchen
        if(!admins.stream().iterator().hasNext() && versuchCount > 4) {

            anmeldungProtokol(nameOrEmalOrTelefon);
            versuchCount = 0;
        }
        versuchCount = versuchCount + 1;

        //System.out.println("return Admin Service: " + admins.map(Admin::getUsername).orElse("Return, Admin Service"));
        return admins.map(AdminDetails::new).orElseThrow(()->new UsernameNotFoundException("Name, Email oder Telefon existiert nicht"));
    }


    /**
     *  Einloggen mit falschem Namen in H2-Database/AdminLogin Speichern
     *  gesendet an AdminLoginService
     */
    private void anmeldungProtokol(String inputValue) {

        adminLoginService.anmeldenProtokolieren("falschename", inputValue);
        //System.out.println("Admin Service: " + versuchCount);
    }
}
