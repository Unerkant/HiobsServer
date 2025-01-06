package HiobsServer.controller;

import HiobsServer.configuration.SocketEventListener;
import HiobsServer.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Den 15.09.2024
 */

@Controller
public class AdminController {

    @Autowired
    private SocketEventListener eventListener;
    @Autowired
    private AdminRepository adminRepository;


    @GetMapping(value = { "/", "/admin", "/admin{path}" })
    public String getAdmin(Model model, @RequestParam(required = false) String path){

        //System.out.println("Admin Controller, Path: " + path);
        model.addAttribute("clientOnline", "Online: " + eventListener.getClientCount());

        //adminRepository.findAll();
        //usern = usernService.findeAlle();
        //System.out.println("Admin: " + adminRepository.findAll());

        return "admin";
    }

}
