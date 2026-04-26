package HiobsServer.repository;

import HiobsServer.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * Den 19.12.2025
 * umstieg auf Database: MongoDB
 */

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    // Für den Self-Storage (Nur eine Richtung)
    List<Message> findBySenderIdAndRecipientId(String senderId, String recipientId, Pageable pageable);

    // Für den normalen Chat (Beide Richtungen)
    List<Message> findBySenderIdAndRecipientIdOrSenderIdAndRecipientId(
            String s1, String r1, String s2, String r2, Pageable pageable);

}
