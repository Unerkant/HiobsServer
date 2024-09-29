package HiobsServer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Den 15.09.2024
 */

@Controller
public class AdminController {

    @GetMapping(value = { "/", "/admin", "/admin{path}" })
    public String getAdmin(Model model, @RequestParam(required = false) String path){

        //System.out.println("Admin Controller, Path: " + path);

        return "admin";
    }
}
