package HiobsServer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "channels")
public class Channel {

    @Id
    private String serverId; // entspricht der generierten recipientId
    private String channelName;
    private String channelText;
    private String ownerId; // ID des Erstellers
    private Instant datum = Instant.now();
    private String channelPicture; // Für spätere Kanal-Bilder

    public Channel() {}

    public Channel(String serverId, String channelName, String channelText,
                   String ownerId, Instant datum, String channelPicture) {
        this.serverId = serverId;
        this.channelName = channelName;
        this.channelText = channelText;
        this.ownerId = ownerId;
        this.datum = datum;
        this.channelPicture = channelPicture;
    }

    // Getter & Setter
    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public String getChannelText() { return channelText; }
    public void setChannelText(String channelText) { this.channelText = channelText; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public Instant getDatum() { return datum; }
    public void setDatum(Instant datum) { this.datum = datum; }

    public String getChannelPicture() { return channelPicture; }
    public void setChannelPicture(String channelPicture) { this.channelPicture = channelPicture; }

    @Override
    public String toString() {
        return "Channel{" +
                "serverId='" + serverId + '\'' +
                ", channelName='" + channelName + '\'' +
                ", channelText='" + channelText + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", datum=" + datum +
                ", channelPicture='" + channelPicture + '\'' +
                '}';
    }
}
