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
        // Wir sortieren IMMER nach Zeitstempel absteigend (neueste zuerst)
        //Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        // Wichtig: Sort.by statt new Sort
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));

        try {
            if ("self_storage".equals(partnerId)) {
                // FALL A: Notizen an mich selbst
                //System.out.println("⚓️ Lade Self-Storage für: " + myId);
                return messageRepository.findBySenderIdAndRecipientId(myId, "self_storage", pageable);
            } else {
                // FALL B: Normaler Funkverkehr mit Partner
                return messageRepository.findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
                        myId, partnerId, partnerId, myId, pageable
                );
            }
        } catch (Exception e) {
            //System.err.println("❌ Fehler beim Laden der History: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
