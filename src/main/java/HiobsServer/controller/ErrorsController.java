package HiobsServer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Den 24.09.2024
 */

@RestController
public class ErrorsController {

    // funktioniert nicht, wenn keine lösung gibst dann löschen
    private static final String[] ERR_LINK = {
            "/{errLink}",
            "/admin/{errLink}",
            "/entwickler/{errLink}",
            "/statistik/{errLink}"
    };

    /**
     * Manipulierte Link abfangen und error Seite ausgeben, nur Admin Teil
     * @param errLink
     * @param model
     * @return
     */
    @GetMapping(value = { "/{errLink}", "/admin/{errLink}", "/developer/{errLink}", "/statistik/{errLink}", "/support/{errLink}" })
    public String error(@PathVariable("errLink") String errLink, Model model){

        StringBuffer path = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURL();
        model.addAttribute("linkName", path);

        //System.out.println("ERROR VON ERROR CONTROLLER: " + errLink );
        return "errors";
    }


    /**
     * Manipulierte Post Request abfangen von HiobsClient...oder von anderer APP
     * @param errPost
     * @return
     */

    // Hatte nicht funktioniert
    private static final String[] ERR_POST = {
            "/{errPost}",
            "/loginMail/{errPost}",
            "/loginSave/{errPost}"
    };

    @PostMapping(value = {"/{errPost}", "/loginMail/{errPost}", "/loginSave/{errPost}"})
    public ResponseEntity<String> postError(@PathVariable("errPost") String errPost ){

       System.out.println("@PostMapping: " + errPost);
       return ResponseEntity.status(HttpStatus.OK).body("noPostRequest");
    }

}
