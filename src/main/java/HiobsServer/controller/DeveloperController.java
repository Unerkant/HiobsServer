package HiobsServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Den 14.01.2024
 */

@Controller
public class DeveloperController {

    @GetMapping(value = "/developer")
    public String getDeveloper() {

        return "developer";
    }
}
