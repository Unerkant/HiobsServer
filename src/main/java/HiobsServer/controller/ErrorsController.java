package HiobsServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Den 24.09.2024
 */

@Controller
public class ErrorsController {

    // funktioniert nicht, wenn keine lösung gibst dann löschen
    private static final String[] ERR_LINK = {
            "/{errLink}",
            "/admin/{errLink}",
            "/entwickler/{errLink}",
            "/statistik/{errLink}"
    };

    @GetMapping(value = { "/{errLink}", "/admin/{errLink}", "/entwickler/{errLink}", "/statistik/{errLink}" })
    public String error(@PathVariable("errLink") String errLink, Model model){

        StringBuffer path = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURL();
        model.addAttribute("linkName", path);

        //System.out.println("ERROR VON ERROR CONTROLLER: " + errLink );
        return "errors";
    }
}
