package HiobsServer.service;

import HiobsServer.admin.model.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Den 30.10.2024
 */

public class AdminDetails implements UserDetails {

    private Admin admin;
    public AdminDetails(Admin admin) {
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        /**
         * zugesendet List-Array von AdminService
         * Admin: [Admin{  id=1, datum='30.10.2024 15:10:15', username='Admin', uservorname='null',
         * pseudonym='null', email='admin@admin.de', telefon='null',
         * passwort='$2a$12$9L1myAJRx6/LUKcfmaJ3o.Bi64DC6J0.ZTvvOVfdlzox6ODabC7M.', other='null', role='ROLE_ADMIN'}]
         *
         * return roles; [ROLE_ADMIN]
         * ACHTUNG: in GrantedAuthority wirt die ROLE_ angeschnitten und nur die endung benutzt( ADMIN )
         *
         * VORSICHT: wenn die spalte 'role' in datenbank leer ist dann, ohne if-abfrage geht
         *          die Login-Seite auf error: Whitelabel Error Page
         */
        if (admin.getRole().isBlank()) {
            // Fehler in Login.html Anzeigen
            throw new UsernameNotFoundException("Zugriff verweigert [90070]");
        }
        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority(admin.getRole()));
        return roles;
    }

    @Override
    public String getPassword() {
        return admin.getPasswort();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    /* Konto nicht abgelaufen */
    @Override
    public boolean isAccountNonExpired() {
        return true;  //UserDetails.super.isAccountNonExpired();
    }

    /* Konto nicht gesperrt*/
    @Override
    public boolean isAccountNonLocked() {
        return true;
        //UserDetails.super.isAccountNonLocked();
    }

    /* Konto ist Aktiviert */
    @Override
    public boolean isEnabled() {
        return true; //UserDetails.super.isEnabled();
    }

    /* Konto noch angemeldet */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //UserDetails.super.isCredentialsNonExpired();
    }

}
