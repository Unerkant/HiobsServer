package HiobsServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Den 27.09.2024
 */

@Controller
public class StatistikController {

    @GetMapping(value = {"/statistik", "/statistik{path}"})
    public String getStatistik(Model model, @RequestParam(required = false) String path) {

        //System.out.println("Statistik Controller, Path: " + path);

        return "statistik";
    }
}
