package HiobsServer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "channelmessages")
public class ChannelMsg {

    @Id
    private String id;
    private String channelId; // Die recipientId des Kanals
    private String senderId;  // Wer den Post verfasst hat

    private String content;
    private Instant timestamp = Instant.now();

    private String type;       // "TEXT", "IMAGE", etc.
    private Object fileUrl;
    private String fileName;

    // Deine neuen Feedback-Spalten (Speichert User-IDs, die gevotet haben)
    private List<String> topUserIds = new ArrayList<>();   // Daumen hoch 👍
    private List<String> flopUserIds = new ArrayList<>();  // Daumen runter 👎

    public ChannelMsg() {}

    public ChannelMsg(String id, String channelId, String senderId, String content, Instant timestamp, String type,
                      String fileName, Object fileUrl, List<String> topUserIds, List<String> flopUserIds) {
        this.id = id;
        this.channelId = channelId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.topUserIds = topUserIds;
        this.flopUserIds = flopUserIds;
    }

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Object getFileUrl() { return fileUrl; }
    public void setFileUrl(Object fileUrl) { this.fileUrl = fileUrl; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public List<String> getTopUserIds() { return topUserIds; }
    public void setTopUserIds(List<String> topUserIds) { this.topUserIds = topUserIds; }

    public List<String> getFlopUserIds() { return flopUserIds; }
    public void setFlopUserIds(List<String> flopUserIds) { this.flopUserIds = flopUserIds; }

    @Override
    public String toString() {
        return "ChannelMsg{" +
                "id='" + id + '\'' +
                ", channelId='" + channelId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", fileUrl=" + fileUrl +
                ", fileName='" + fileName + '\'' +
                ", topUserIds=" + topUserIds +
                ", flopUserIds=" + flopUserIds +
                '}';
    }
}
