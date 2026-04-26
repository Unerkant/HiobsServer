package HiobsServer.controller;

import HiobsServer.dto.UserStatus;
import HiobsServer.model.Message;
import HiobsServer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Den 9.2.2026
 */

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageService messageService;
    
    /**
     *  Message entgegen nehmen und weiter leiten, HiobsClient/stompConnection.js
     * @param message
     */
    @MessageMapping("/messages")
    public void messageReceiving(Message message) {

        // 1. Speichern in MongoDB
        Message savedMsg = messageService.saveNewMessage(message);

        // 2. An den Absender zurückschicken (Bestätigung für das UI)
        messagingTemplate.convertAndSend("/messages/receive/" + savedMsg.getSenderId(), savedMsg);

        // 3. An den Empfänger zustellen (falls es kein Self-Storage ist)
        if (!savedMsg.getRecipientId().equals("self_storage")) {
            messagingTemplate.convertAndSend("/messages/receive/" + savedMsg.getRecipientId(), savedMsg);
        }
    }

    /**
     * User Online (grünes Licht an ProfilBild oder Pseudonym), hiobsClient/stompConnection.js
     * @param status
     */
    @MessageMapping("/user.online")
    public void userOnlineHandler(UserStatus status, SimpMessageHeaderAccessor headerAccessor) {

        // 1. Die UserId in die Session-Attribute schreiben
        // Das ist der "Stempel" im Logbuch dieser speziellen Verbindung
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("userId", status.getUserId());
        }

        // 2. Status auf online setzen
        status.setOnline(true);

        //System.out.println("⚓️ Login-Session für User: " + status.getUserId());

        // 3. An alle verteilen
        messagingTemplate.convertAndSend("/topic/user-status", status);
    }

    /**
     * User Offline (grünes Licht ausblenden), hiobsClient/stompConnection.js
     * @param status
     */
    @MessageMapping("/user.offline")
    public void userOfflineHandler(UserStatus status) {

        // Wir erzwingen den Status auf 'false'
        status.setOnline(false);

        //System.out.println("⚓️ User geht von Bord: " + status.getUserId());

        // Und ab damit in den Funkkanal, damit alle Augen grau werden
        messagingTemplate.convertAndSend("/topic/user-status", status);
    }

}
