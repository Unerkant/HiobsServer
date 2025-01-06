package HiobsServer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Den 15.09.2024
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

    private static final String[] AUTH_WHITELIST = {
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
                        .requestMatchers(HttpMethod.POST, "/**").permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/entwickler/**").hasAnyRole("ENTWICKLER", "ADMIN")
                        .requestMatchers("/statistik/**").hasAnyRole("STATISTIK", "ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .formLogin( form -> form
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/default", true)  /* extra default class */
                        .failureUrl("/errors")
                        .permitAll()
                )
                .logout( logout -> logout
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );
        return http.build();

    }

}
