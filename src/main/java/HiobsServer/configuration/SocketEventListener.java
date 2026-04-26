package HiobsServer.configuration;

import HiobsServer.dto.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Den 1.10.2024
 */

@Component
public class SocketEventListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public Integer clientCount = 0;
    public Integer getClientCount() { return clientCount; }
    public void setClientCount(Integer clientCount) { this.clientCount = clientCount; }

    /**
     * count alle user, für den Admit Teil
     * @param event
     */

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        clientPlus(clientCount);
        //System.out.println("Event Listener Connect: " + event);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        clientMinus(clientCount);
        //System.out.println("Event Listener Disconnect: " + event);
    }

    private void clientPlus(Integer plus) {
        clientCount = plus + 1;
        setClientCount(clientCount);
    }

    private void clientMinus(Integer minus) {
        clientCount = minus -1;
        setClientCount(clientCount);
    }

    /**
     * User Offline,
     * für HiobsClient/stompConnection.js/stompClient.subscribe('/topic/user-status', function(output){...}
     * @param event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Spring weiß, welche Session beendet wurde
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // Hier wird es trickreich: Du musst beim Verbinden (Connect) die UserId
        // in der Session Attributes speichern, um sie hier wiederzufinden.
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        if (userId != null) {
            UserStatus status = new UserStatus();
            status.setUserId(userId);
            status.setOnline(false);

            //System.out.println("⚓️ Verbindung verloren (Timeout/Abbruch): " + userId);
            simpMessagingTemplate.convertAndSend("/topic/user-status", status);
        }
    }
}
