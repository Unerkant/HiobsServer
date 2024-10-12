package HiobsServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Den 24.09.2024
 */

@Controller
public class WartungController {

    @GetMapping(value = { "/wartung", "/wartung{path}" })
    public String getWartung(Model model, @RequestParam(required = false) String path) {

        /**
         * path -> angehängte an URL einen get Parameter
         * z.b.s. http://localhost:8080/wartung?path=getParameter
         * output: getParameter
         */
        //System.out.println("Path(wartung): " + path);

        return "wartung";
    }
}
