package HiobsServer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Den 15.09.2024
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Öffentlich erreichbare Seiten / Ressourcen (Öffentliches Web-Frontend)
    private static final String[] AUTH_WHITELIST = {
            "/profil/**",
            "/profilbild/**",     // NEU: Zum Laden der Profilbilder (<img src="...">)
            "/register/**",
            "/h2-console/**",
            "/resources/**",
            "/css/**",
            "/js/**",
            "/images/**"
    };

    // 2. API-Endpunkte, die KEINE CSRF-Token-Prüfung benötigen (z. B. für HiobsClient REST-Calls)
    private static final String[] API_CSRF_IGNORE_WHITELIST = {
            "/exceptionen/**",
            "/h2-console/**",
            "/loginMail",
            "/loginSave",
            "/letzteLogin",
            "/letzteLoginSave",
            "/sperreDeleteApi",
            "/allFriends/all",
            "/oneFriends/*",
            "/historyMessages/**",
            "/userInfo/**",
            "/friends/**",
            "/channel/**",
            "/profil/**"     // NEU: Für /profil/upload und /profil/bildDelete
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Saubere CSRF-Konfiguration (ignoriert CSRF nur für API-Aufrufe)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(API_CSRF_IGNORE_WHITELIST)
                )
                // H2-Console Frame-Optionen freischalten
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Authorisierung / Rechtevergabe
                .authorizeHttpRequests(authorize -> authorize
                        // Öffentliche URLs erlauben (GET, POST etc.)
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(API_CSRF_IGNORE_WHITELIST).permitAll()

                        // Rollengeschützte Bereiche
                        .requestMatchers("/developer/**").hasAnyRole("DEVELOPER", "ADMIN")
                        .requestMatchers("/support/**").hasAnyRole("SUPPORT", "ADMIN")
                        .requestMatchers("/statistik/**").hasAnyRole("STATISTIK", "ADMIN")
                        .requestMatchers("/", "/admin/**").hasRole("ADMIN")

                        // Alle übrigen Anfragen müssen authentifiziert sein!
                        .anyRequest().authenticated()
                )
                // Formular-Login für Web-Nutzer
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/default", true)
                        .failureHandler(authenticationFailureHandler())
                        .failureUrl("/error")
                        .permitAll()
                )
                // Logout-Steuerung
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AdminFailureHandler();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
