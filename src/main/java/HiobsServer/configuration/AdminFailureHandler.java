package HiobsServer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Den 4.05.2025
 */

/**
 * ACHTUNG: am 5.05.2025 ist der AdminAuthenticationFailureHandler ausgesetzt
 * WEITERE EINSTELLLUNGEN: SecurityConfig Zeile: 52 + 74 die function:
 *      @Bean
 *     public AuthenticationFailureHandler authenticationFailureHandler(){...}
 *
 *     WARUM AUSGESETZT:
 *     1. bei falsche namen angabe werde keine fehler von spring boot security angezeigt
 *     2. Safari Browser: bei Einloggen mit falschen Namen, den Klick auf sing up hatte
 *          Download ausgelöst statt die error seite anzeigen
 *     3. Chrome Browser: bei falschen Namen angaben hatte die error seite angezeigt mit den array
 *          {"exception":"Ungültige Anmeldedaten","timestamp":1746387670407}
 *     4. habe nicht herausgefunden wie man nach 3 faschen Name angabe die error seite soll anzeigen,
 *        bei Chrome den 1 versuch mit falschen Namen, werde die error seite angezeigt,
 *        bei Safari nach 1 versuch hatte die download ausgelöst
 */
@Component
public class AdminFailureHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

    /*    response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect("/error");*/

        //String username = request.getParameter("username");
        //System.out.println("Admin Failure Handler: " + username);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", Calendar.getInstance().getTime());
        data.put("exception", exception.getMessage());

        response.getOutputStream().println("Response: " +objectMapper.writeValueAsString(data));
        //redirectStrategy.sendRedirect(request, response, "/login?error=true");
        System.out.println("Failure Handler: " + response);
    }


}
