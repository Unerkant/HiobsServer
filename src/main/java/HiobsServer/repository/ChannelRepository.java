// ChannelRepository.java
package HiobsServer.repository;

import HiobsServer.model.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChannelRepository extends MongoRepository<Channel, String> {

    // Findet alle Kanäle, bei denen die ownerId mit der übergebenen User-ID übereinstimmt
    List<Channel> findByOwnerId(String ownerId);

    // Finde einen Kanal
    List<Channel> findByServerId(String serverId);
}
