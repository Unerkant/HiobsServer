package HiobsServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Den 24.09.2024
 */

@Controller
public class EngineerController {

    @GetMapping(value = { "/engineer", "/engineer{path}" })
    public String getEngineer(Model model, @RequestParam(required = false) String path) {

        /**
         * path -> angehängte an URL einen get Parameter
         * z.b.s. http://localhost:8080/engineer?path=getParameter
         * output: getParameter
         */
        System.out.println("Path(engineer): " + path);

        return "engineer";
    }
}
