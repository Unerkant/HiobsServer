package HiobsServer.configuration;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Den 1.10.2024
 */

@Component
public class SocketEventListener {

    public Integer clientCount = 0;
    public Integer getClientCount() { return clientCount; }
    public void setClientCount(Integer clientCount) { this.clientCount = clientCount; }

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
}
