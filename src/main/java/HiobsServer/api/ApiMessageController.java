package HiobsServer.api;

import HiobsServer.model.Message;
import HiobsServer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Den 11.02.2026
 */

@RestController
public class ApiMessageController {

    @Autowired
    private MessageService messageService;

    /**
     * Liefert den Chat-Verlauf zwischen zwei Usern
     * Aufruf: GET /historyMessages/history?from=...&to=...
     */
    @PostMapping("/historyMessages/history")
    public ResponseEntity<List<Message>> getChatHistory(@RequestBody Map<String, Object> payload) {
        String from = (String) payload.get("from");
        String to = (String) payload.get("to");

        // Initial-Load: Seite 0, 20 Nachrichten
        List<Message> history = messageService.getChatHistory(from, to, 0, 20);

        return ResponseEntity.ok(history);
    }

    /**
     * holt die erste 20 messages, dann rest
     * @param payload
     * @return
     */

    @PostMapping("/historyMessages/Paged")
    public List<Message> getPagedHistory(@RequestBody Map<String, Object> payload) {
        String userA = (String) payload.get("userA");
        String userB = (String) payload.get("userB");
        int page = (int) payload.get("page");
        int size = (int) payload.get("size");

        return messageService.getChatHistory(userA, userB, page, size);
    }
}
