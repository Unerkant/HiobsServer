package HiobsServer.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

/**
 * Den 25.09.2024
 */

@Controller
public class DefaultController {

    @GetMapping(value = "/default")
    public void defaultLogin(HttpServletRequest request, HttpServletResponse servletResponse) throws IOException {

        /**
         * ACHTUNG: die default class ist nur eine hilfe-class für die SecurityConfig class
         * sie regelt welche Seite soll geöffnet werden nach Authorisierung(Role)...
         * z.b.s mit ADMIN-Rechten wird die admin.html geöffnet
         *       mit ROLE_ENGINEER wird die engineer.html geöffnet nicht andere
         *       mit ROLE_ENTWICKLER nur die support.html Seite
         * .defaultSuccessUrl("/default", true) → SecurityConfig/Zeile: 56
         *
         * request.isUserInRole → true/false
         */
        if (request.isUserInRole("ROLE_ADMIN")) {

            //System.out.println("Admin: " + request.isUserInRole("ROLE_ADMIN"));
            //return "admin";
            servletResponse.sendRedirect("admin");

        } else if (request.isUserInRole("ROLE_DEVELOPER")) {

            servletResponse.sendRedirect("developer");

        } else if (request.isUserInRole("ROLE_SUPPORT")) {

            servletResponse.sendRedirect("support");

        } else if (request.isUserInRole("ROLE_STATISTIK")) {

            servletResponse.sendRedirect("statistik");

        } else {
            //System.out.println("Admin: " + request.isUserInRole("ROLE_ADMIN"));
            //return "redirect:/login";
            servletResponse.sendRedirect("errors");
        }
    }
}
