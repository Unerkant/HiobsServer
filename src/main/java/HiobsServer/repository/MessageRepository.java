package HiobsServer.repository;

import HiobsServer.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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


    // Abfrage für HiobsClient/UserInfoController
    // WICHTIG: 'recipientId' statt 'receiverId' nutzen!
    @Query(value = "{$or: [ {senderId: ?0, recipientId: ?1}, {senderId: ?1, recipientId: ?0} ]}", sort = "{timestamp: -1}")
    List<Message> findChatHistory(String id1, String id2);


    // chat Verlauf Löschen, MsgController(HiobsClient)
    @Query("{ '$or': [ { 'senderId': ?0, 'recipientId': ?1 }, { 'senderId': ?1, 'recipientId': ?0 } ] }")
    List<Message> findByParticipants(String userA, String userB);


    // suche nach letzte Message von meinem Freunde
    @Query("{ '$or': [ { 'senderId': ?0, 'recipientId': ?1 }, { 'senderId': ?1, 'recipientId': ?0 } ] }")
    List<Message> findLatestMessageBetweenUsers(String userA, String userB, Pageable pageable);

    // Zählt ungelesene Nachrichten von einem Absender für den aktuellen Empfänger
    long countByRecipientIdAndSenderIdAndGelesen(String recipientId, String senderId, boolean gelesen);

    // Markiert alle Message als Gelesen
    List<Message> findByRecipientIdAndSenderIdAndGelesen(String myId, String friendId, boolean b);

    // NEUE: die Datenbank-Abfrage gruppiert
    @Query("{ '$or': [ { 'senderId': ?0, 'recipientId': ?1 }, { 'senderId': ?1, 'recipientId': ?0 } ] }")
    List<Message> findHistoryBetweenUsers(String userA, String userB, Pageable pageable);
}
