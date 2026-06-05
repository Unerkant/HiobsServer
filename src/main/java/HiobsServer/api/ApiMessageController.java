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
     */
    @PostMapping("/historyMessages/Paged")
    public List<Message> getPagedHistory(@RequestBody Map<String, Object> payload) {
        String userA = (String) payload.get("userA");
        String userB = (String) payload.get("userB");
        int page = (int) payload.get("page");
        int size = (int) payload.get("size");

        return messageService.getChatHistory(userA, userB, page, size);
    }

    /**
     *  einzelne/ausgewählte Message Löschen
     */
    @PostMapping("/historyMessages/msgDelete")
    public ResponseEntity<List<Message>> deleteHistory(@RequestBody Map<String, Object> payload) {
        // payload kommt an: { "ids": ["id1", "id2"] }
        List<Message> deletedMsgs = messageService.deleteHistory(payload);

        return ResponseEntity.ok(deletedMsgs);
    }


    /**
     * Alle Messages Löschen (Verlauf leeren)
     */
    @PostMapping("/historyMessages/allMsgDelete")
    public ResponseEntity<List<Message>> deleteAllHistory(@RequestBody Map<String, Object> payload) {
        // Parameter aus dem Payload extrahieren
        String from = (String) payload.get("from");
        String to = (String) payload.get("to");

        // Service-Aufruf: Inventur und Löschung in einem Durchgang
        List<Message> deletedMessages = messageService.deleteAllHistory(from, to);

        // Wir senden die gelöschten Nachrichten zurück,
        // damit der Client die Dateien aufräumen kann
        return ResponseEntity.ok(deletedMessages);

        /**
         * deletedMessages: [Message{id='6a2177dac727be760ac9d951', senderId='69962ec8360a87668ab19142',
         * recipientId='self_storage', content='x bxcy yc yc yc', timestamp=2026-06-04T13:04:26.874Z,
         * type='TEXT', fileUrl='null', fileName='null', base64Data='null', gelesen=false},
         * Message{id='6a2177ddc727be760ac9d952', senderId='69962ec8360a87668ab19142', recipientId='self_storage',
         * content='y c yc yc yc yç', timestamp=2026-06-04T13:04:29.123Z, type='TEXT', fileUrl='null',
         * fileName='null', base64Data='null', gelesen=false}, Message{id='6a2177e6c727be760ac9d953',
         * senderId='69962ec8360a87668ab19142', recipientId='self_storage', content='',
         * timestamp=2026-06-04T13:04:38.924Z, type='GALLERY',
         * fileUrl='[1780578278910_6pg.jpg, 1780578278914_33.jpg, 1780578278914_99.jpg]',
         * fileName='null', base64Data='null', gelesen=false}]
         */
    }

}
