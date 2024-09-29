package HiobsServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Den 24.09.2024
 */

@Controller
public class ErrorsCongroller {

    private static final String[] ERR_LINK = {
            "/{errLink}",
            "/admin/{errLink}",
            "/other/{errLink}",
            "/verwaltung/{errLink}"
    };

    @GetMapping(value = { "/{errLink}", "/admin/{errLink}", "/other/{errLink}", "/verwaltung/{errLink}" })
    public String error(@PathVariable("errLink") String errLink, Model model){

        model.addAttribute("linkName", errLink);

        //System.out.println("ERROR VON ERROR CONTROLLER: " + errLink );
        return "errors";
    }
}
