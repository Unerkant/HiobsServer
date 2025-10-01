package HiobsServer.controller;


import HiobsServer.secondary.model.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Den 28.09.2024
 */

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/messages")
    public void messageReceiving(Messages messages) {

        //System.out.println("Message Controller: " + message);
        simpMessagingTemplate.convertAndSend("/messages/receive/" + messages.getFreundetoken(), messages);
    }
}
