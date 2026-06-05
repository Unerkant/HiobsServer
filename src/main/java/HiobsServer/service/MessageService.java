package HiobsServer.service;

import HiobsServer.model.Message;
import HiobsServer.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Den 2.02.2026
 */

@Service
public class MessageService {

    @Autowired
    private MessageRepository  messageRepository ;

    /**
     * Save messages
     * @param msg
     * @return
     */
    public Message saveNewMessage(Message msg) {

        if (msg.getTimestamp() == null) {
            msg.setTimestamp(Instant.now());
        }
        return messageRepository.save(msg);
    }

    /**
     * Messages Laden
     * @param myId
     * @param partnerId
     * @return
     */
    public List<Message> getChatHistory(String myId, String partnerId, int page, int size) {
        //  Wir sortieren IMMER nach Zeitstempel absteigend (neueste zuerst)
        //  Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        //  Wichtig: Sort.by statt new Sort
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));

        try {
            if ("self_storage".equals(partnerId)) {
                // FALL A: Notizen an mich selbst
                //System.out.println("⚓️ Lade Self-Storage für: " + myId);
                return messageRepository.findBySenderIdAndRecipientId(myId, "self_storage", pageable);
            } else {
                // FALL B: normaler Funkverkehr mit Partner
                return messageRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
                        myId, partnerId, partnerId, myId, pageable
                );
            }
        } catch (Exception e) {
            //System.err.println("❌ Fehler beim Laden der History: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * für HiobsClient/UserInfoController
     * @param userA
     * @param userB
     * @return
     */
    public List<Message> getUserHistory(String userA, String userB) {
        // Wir suchen alle Nachrichten, wo (Sender=A UND Empfänger=B) ODER (Sender=B UND Empfänger=A)
        // und wir sortieren direkt nach Zeitstempel absteigend (neueste zuerst)
        return messageRepository.findChatHistory(userA, userB);
    }

    /**
     * ACHTUNG: nur für den Test oder gesamt Messages in MongoDB ermitteln oder count-messages von mir undFreund
     * @param userA
     * @param userB
     * @return
     */
    public List<Message> getHistoryCount(String userA, String userB) {

        // DEBUG: Wie viele Nachrichten gibt es überhaupt in der DB?
        long gesamt = messageRepository.count();
        System.out.println("⚓️ DEBUG: Gesamtzahl Nachrichten in DB: " + gesamt);

        List<Message> history = messageRepository.findChatHistory(userA, userB);
        System.out.println("⚓️ DEBUG: Gefundene History: " + history.size() + " Einträge");

        /**
         * ⚓️ DEBUG: Gesamtzahl Nachrichten in DB: 303  (stand: 5.5.2026)
         * ⚓️ DEBUG: gefundene History: 0 Einträge
         */

        return history;
    }


    /* *********************** Delete Methoden ********************* */
    /**
     * einzelne/ausgewählte Messages Löschen
     */
    public List<Message> deleteHistory(Map<String, Object> payload) {
        List<String> ids = (List<String>) payload.get("ids");
        List<Message> deletedList = new ArrayList<>();

        for (String id : ids) {
            Optional<Message> msg = messageRepository.findById(id);
            if (msg.isPresent()) {
                Message m = msg.get();
                deletedList.add(m); // Speichere Objekt für den Client
                messageRepository.delete(m); // Lösche aus MongoDB
            }
        }
        return deletedList;
    }

    /**
     *  Chat-Verlauf Löschen
     */
    public List<Message> deleteAllHistory(String userA, String userB) {
        // 1. Suche alle Nachrichten zwischen A und B (in beide Richtungen)
        // Deine Query im Repository muss beide Kombinationen abdecken
        List<Message> allMsgs = messageRepository.findByParticipants(userA, userB);

        // 2. Wir geben die Liste zurück, BEVOR wir sie löschen,
        // damit der Client die 'fileUrl' Informationen noch hat!
        List<Message> copyForCleanup = new ArrayList<>(allMsgs);

        // 3. Datenbank bereinigen
        messageRepository.deleteAll(allMsgs);

        return copyForCleanup;
    }
}
