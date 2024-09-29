package HiobsServer.service;

import HiobsServer.model.Developer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Den 15.09.2024
 */

public class MyDeveloperDetails implements UserDetails {

    private Developer developer;

    public MyDeveloperDetails(Developer developer){
        this.developer = developer;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        /**
         * output: developer{  id=2, datum='', usertoken='', messagetoken='', username='Sam', uservorname='',
         * pseudonym='', email='', telefon='', passwort='$2a$12$N2sg57Rtv493Kk7Y3/keKeQaTJHxzbO2a9s4l8fsxx8wNnVZfJ3j2',
         * other='', role='[Roles{  id=2, authority='ROLE_ENGINEER'}]'}
         *
         *         return developer.getRole.... : [ROLE_ENGINEER]
         *         ACHTUNG: in SecurityConfig wirt die ROLE_ angeschnitten und nur die endung benutzt( ENGINEER)
         */
        return developer
                .getRole()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return developer.getPasswort();
    }

    @Override
    public String getUsername() {
        return developer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        //return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        //return UserDetails.super.isEnabled();
        return true;
    }
}
