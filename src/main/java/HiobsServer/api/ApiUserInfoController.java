package HiobsServer.api;

import HiobsServer.model.Message;
import HiobsServer.repository.MessageRepository;
import HiobsServer.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Den 13.05.2026
 */

@RestController
public class ApiUserInfoController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;

    /**
     * für HiobsClient/UserInfoController
     * zugesendet: {freundId=6989c6273217d0d4651d7e42, meineId=69962ec8360a87668ab19142}
     * oder
     * zugesendet: {freundId=system_hiobs, meineId=69962ec8360a87668ab19142}
     */
    @PostMapping("/userInfo/infoHistory")
    public List<Message> getUserInfo(@RequestBody Map<String, Object> payload) {

        String meineId = (String) payload.get("meineId");
        String freundId = (String) payload.get("freundId");

        if (meineId == null || freundId == null) {
            return new ArrayList<>();
        }

        // Übergabe an den Service, der die DB-Abfrage macht
        return messageService.getUserHistory(meineId, freundId);
    }


    /**
     * für HiobsClient/UserInfoController/ @PostMapping(path = "/userinfo/mediendelete")
     * hier werden die Medien von User Information in MongoDB/Message gelöscht,
     * dann zurück zu Hiobs/client/UserInfoController/ @PostMapping(path = "/userinfo/mediendelete") und
     * Restlichen gespeicherte Dateien in uploads/** (HiobsClient) gelöscht
     *
     * ARRAY-FORMAT: zugesendet, bearbeitet & save in MongoDB das gleiche Format(mit eckigen klammer)
     * itemsToDelete: [6a0cb840f4f7e97f73a77a5e:1779218496667_IMG_6405.jpeg, 6a0cb840f4f7e97f73a77a5e:1779218496673_IMG_6404.jpeg]
     *
     */
    @PostMapping("/userInfo/infoDelete")
    public ResponseEntity<Map<String, Object>> getInfoDelete(@RequestBody Map<String, List<String>> payload) {
        List<String> itemsToDelete = payload.get("items");
        Map<String, Object> response = new HashMap<>();

        if (itemsToDelete == null || itemsToDelete.isEmpty()) {
            response.put("success", false);
            response.put("message", "Keine Elemente zum Löschen empfangen.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            for (String item : itemsToDelete) {
                String[] parts = item.split(":");
                if (parts.length < 2) continue;

                String msgId = parts[0];
                String fileOrLink = parts[1];

                Optional<Message> msgOpt = messageRepository.findById(msgId);
                if (msgOpt.isPresent()) {
                    Message msg = msgOpt.get();

                    // Fall A: Es ist ein Link (TEXT-Nachricht) → Ganze Nachricht löschen
                    if ("link".equals(fileOrLink) || "TEXT".equals(msg.getType())) {
                        messageRepository.delete(msg);
                    }
                    // Fall B: Dateitypen (MEDIEN, DATEI, AUDIO)
                    else {
                        String currentUrls = msg.getFileUrl().toString();
                        if (currentUrls != null) {
                            // Wenn nur eine Datei existiert oder der String exakt übereinstimmt
                            if (currentUrls.equals(fileOrLink) || !currentUrls.contains(",")) {
                                messageRepository.delete(msg);
                            } else {

                                // 1. Hole den aktuellen Wert (der kann String ODER List sein)
                                Object rawUrls = msg.getFileUrl();
                                String curentUrl = (rawUrls != null) ? rawUrls.toString() : "";

                                // 2. Den String von ALLEN Störfaktoren befreien (eckige Klammern und Reste)
                                String cleanUrls = curentUrl.replaceAll("[\\[\\]]", "");

                                // 3. Split, Filter und Liste erstellen
                                List<String> updatedUrlsList = Arrays.stream(cleanUrls.split(","))
                                        .map(String::trim)
                                        .filter(url -> !url.isEmpty()) // Leerstellen entfernen
                                        .filter(url -> !url.equalsIgnoreCase(fileOrLink.trim())) // Das gelöschte Element raus
                                        .collect(Collectors.toList());

                                // 4. Jetzt die entscheidende Weiche:
                                // Wenn nur noch eine Datei übrig ist, speichern wir sie als String (für Kompatibilität),
                                // wenn mehrere da sind, speichern wir die Liste.
                                if (updatedUrlsList.size() == 1) {
                                    msg.setFileUrl(updatedUrlsList.get(0));
                                } else if (updatedUrlsList.isEmpty()) {
                                    // Wenn alles gelöscht wurde, Nachricht entfernen
                                    messageRepository.delete(msg);
                                    return ResponseEntity.ok(Map.of("success", true));
                                } else {
                                    msg.setFileUrl(updatedUrlsList);
                                }

                                // 5. Finales Speichern
                                messageRepository.save(msg);

                            }
                        }
                    }
                }
            }
            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
