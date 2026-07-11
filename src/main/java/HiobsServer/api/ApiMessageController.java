package HiobsServer.api;

import HiobsServer.model.Message;
import HiobsServer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
    public ResponseEntity<Map<LocalDate, List<Message>>> getChatHistory(@RequestBody Map<String, Object> payload) {
        String from = (String) payload.get("from");
        String to = (String) payload.get("to");

        // Initial-Load: Seite 0, 20 Nachrichten
        Map<LocalDate, List<Message>> history = messageService.getGroupedChatHistory(from, to, 0, 20);

        return ResponseEntity.ok(history);
        /*
         * ACHTUNG: DIESER FORMAT WIRD AN HIOBSCLIENT GESENDET
         * History: {2026-06-11=[Message{id='6a2b02fa7fd3855c3f5c4a6c', senderId='69962ec8360a87668ab19142',
         * recipientId='6989c6273217d0d4651d7e42', content='OK, hier ist der Chrome von google!',
         * timestamp=2026-06-11T18:48:26.422Z, type='TEXT', fileUrl='null',
         * fileName='null', base64Data='null', gelesen=true}],
         * 2026-06-14=[Message{id='6a2e7961454d3c638cfbfd9e', senderId='69962ec8360a87668ab19142',
         * recipientId='6989c6273217d0d4651d7e42', content='Hallo Safari hier ist der Chrome',
         * timestamp=2026-06-14T09:50:25.834Z, type='TEXT', fileUrl='null', fileName='null',
         * base64Data='null', gelesen=true}]}
         */
    }

    /**
     * holt die erste 20 messages, dann rest
     */
    @PostMapping("/historyMessages/Paged")
    public ResponseEntity<Map<LocalDate, List<Message>>> getPagedHistory(@RequestBody Map<String, Object> payload) {
        String userA = (String) payload.get("userA");
        String userB = (String) payload.get("userB");
        int page = (int) payload.get("page");
        int size = (int) payload.get("size");

        Map<LocalDate, List<Message>> groupedHistory = messageService.getGroupedChatHistory(userA, userB, page, size);

        return ResponseEntity.ok(groupedHistory);
        /*
         * ACHTUNG: DIESER FORMAT WIRD AN HIOBSCLIENT GESENDET
         * History: {2026-06-11=[Message{id='6a2b02fa7fd3855c3f5c4a6c', senderId='69962ec8360a87668ab19142',
         * recipientId='6989c6273217d0d4651d7e42', content='OK, hier ist der Chrome von google!',
         * timestamp=2026-06-11T18:48:26.422Z, type='TEXT', fileUrl='null',
         * fileName='null', base64Data='null', gelesen=true}],
         * 2026-06-14=[Message{id='6a2e7961454d3c638cfbfd9e', senderId='69962ec8360a87668ab19142',
         * recipientId='6989c6273217d0d4651d7e42', content='Hallo Safari hier ist der Chrome',
         * timestamp=2026-06-14T09:50:25.834Z, type='TEXT', fileUrl='null', fileName='null',
         * base64Data='null', gelesen=true}]}
         */
    }


    /**
     *  ungelesene message als gelesen setzen(true)
     */
    @PostMapping("/historyMessages/allGelesen")
    public void getAllGelesenHistory(@RequestBody Map<String, Object> payload) {
        String userA = (String) payload.get("userA");
        String userB = (String) payload.get("userB");

        messageService.markiereAlsGelesen(userA, userB);
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
        // damit der Client die Dateien(uploads) aufräumen kann
        return ResponseEntity.ok(deletedMessages);

    }
}
