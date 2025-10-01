package HiobsServer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Den 15.09.2024
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/profilbild/**",
            "/register/**",
            "/h2-console/**",
            "/resources/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                //.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frameoption -> frameoption.disable()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/**").permitAll()
                        .requestMatchers("/developer/**").hasAnyRole("DEVELOPER","ADMIN")
                        .requestMatchers("/support/**").hasAnyRole("SUPPORT", "ADMIN")
                        .requestMatchers("/statistik/**").hasAnyRole("STATISTIK", "ADMIN")
                        .requestMatchers("/","/admin/**").hasAnyRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .formLogin( form -> form
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/default", true)   /* extra default class */
                        //.failureHandler(authenticationFailureHandler())                /* falsch einloggen Daten */
                        .failureUrl("/error")
                        .permitAll()
                )
                .logout( logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();

    }

    /**
     * ACHTUNG: am 5.05.2025 ausgesetzt + configuration/AdminAuthenticationFailureHandler() + hier Zeile: 52
     *
     * Falsche einloggen Daten
     *
     * QUELLE: configuration: AdminAuthenticationFailureHandler
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AdminFailureHandler();
    }

    /**
     * password codierung
     * https://bcrypt-generator.com/
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

}
