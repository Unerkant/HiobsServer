package HiobsServer.service;

import HiobsServer.model.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
         */

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

    @Override
    public boolean isAccountNonExpired() {
        return true;  //UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  //UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return true; //UserDetails.super.isEnabled();
    }
}
