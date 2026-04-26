package HiobsServer.controller;

import HiobsServer.configuration.SocketEventListener;
import HiobsServer.utilities.MyUtilities;
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
    private MyUtilities myUtilities;


    @GetMapping(value = { "/", "/admin", "/admin{path}" })
    public String getAdmin(Model model, @RequestParam(required = false) String path){

        //System.out.println("Admin Controller, Path: " + path);
        model.addAttribute("clientOnline", "Online: " + eventListener.getClientCount());

        model.addAttribute("mytoken", myUtilities.userToken());
        model.addAttribute("msgtoken", myUtilities.messageToken());
        model.addAttribute("hiobstoken", myUtilities.msgHiobsToken());
        model.addAttribute("millis", myUtilities.msgGespeichertesToken());

        System.out.println("Admin Controller: ");

        return "admin";
    }

}
