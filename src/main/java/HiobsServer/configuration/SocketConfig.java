package HiobsServer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Den 28.09.2024
 */

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/messages");
        config.setApplicationDestinationPrefixes("/app");
        //System.out.println("Web Socket Broker Zeile: 18  / " + config);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/register").setAllowedOriginPatterns("*").withSockJS();
        //System.out.println("Stomp EndPoint Registry Zeile: 26  / " + registry);
    }

}
