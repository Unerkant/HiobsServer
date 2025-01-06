package HiobsServer.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Den 25.09.2024
 */

@Controller
public class DefaultController {

    @GetMapping(value = "/default")
    public String defaultLogin( HttpServletRequest request ) {

        /**
         * ACHTUNG: die default class ist nur eine hilfe-class für die SecurityConfig class
         * sie regelt welche Seite soll geöffnet werden nach Authorisierung(Role)...
         * z.b.s mit ADMIN-Rechten wird die admin.html geöffnet
         *       mit ROLE_ENGINEER wird die engineer.html geöffnet nicht andere
         *       mit ROLE_ENTWICKLER nur die entwickler.html Seite
         * .defaultSuccessUrl("/default", true) -> SecurityConfig/Zeile: 56
         *
         * request.isUserInRole -> true/false
         */
        if (request.isUserInRole("ROLE_ENTWICKLER")) {

            //System.out.println("Verwaltung: " + request.isUserInRole("ROLE_VERWALTUNG"));
            return "redirect:/entwickler";

        } else if (request.isUserInRole("ROLE_STATISTIK")) {

            return "redirect:/statistik";

        } else {

            //System.out.println("Admin: " + request.isUserInRole("ROLE_ADMIN"));
            return "redirect:/admin";
        }
    }
}
