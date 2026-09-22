// ChannelMsgRepository.java
package HiobsServer.repository;

import HiobsServer.model.ChannelMsg;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChannelMsgRepository extends MongoRepository<ChannelMsg, String> {

    List<ChannelMsg> findByChannelIdOrderByTimestampAsc(String channelId);
}
