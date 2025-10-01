package HiobsServer.api;

import HiobsServer.secondary.model.Messages;
import HiobsServer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Den 2.09.2025
 */

@Controller
public class ApiMsgController {

    @Autowired
    private MessageService messageService;

    /**
     * Message Laden
     * Benutzt: http://localhost:8090/msg/20250530213505, HiobsClient/MsgController/msgLaden Zeile: ca. 150
     *
     * @param msgtoken  -> output: 20250530213505
     * @return
     */
    @PostMapping(path = "/msgLaden")
    public ResponseEntity<Iterable<Messages>> msgladen(@RequestBody String msgtoken) {

        Iterable<Messages> msg = messageService.findeMessages(msgtoken);
        System.out.println("Api Message Controller: " + msg);

        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

}
