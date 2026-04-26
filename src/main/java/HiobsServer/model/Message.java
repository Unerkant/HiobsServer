package HiobsServer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Den 19.12.2025
 * Umstieg auf Database: MongoDB
 */


@Document(collection = "messages")
public class Message {

    @Id
    private String id;
    private String senderId;
    private String recipientId;     // Kann User-ID, "system_hiobs" oder "self_storage" sein

    private String content;         // Der Text der Nachricht
    private Instant timestamp = Instant.now();

    // MEDIEN-FELDER
    private String type;            // "TEXT", "IMAGE", "VIDEO", "AUDIO", "PDF"
    private Object fileUrl;         // Pfad zur Datei auf dem Strato-Server
    private String fileName;        // Originalname (z.B. "rechnung.pdf")
    private String base64Data;      // Nur für sehr kleine Daten (z.B. Sprachnachricht-Vorschau)

    private boolean gelesen = false;

    public Message(){}

    public Message(String id, String senderId, String recipientId, String content, Instant timestamp,
                   String type, Object fileUrl, String fileName, String base64Data, boolean gelesen) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.base64Data = base64Data;
        this.gelesen = gelesen;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Object getFileUrl() { return fileUrl; }
    public void setFileUrl(Object fileUrl) { this.fileUrl = fileUrl; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getBase64Data() { return base64Data; }
    public void setBase64Data(String base64Data) { this.base64Data = base64Data; }

    public boolean isGelesen() { return gelesen; }
    public void setGelesen(boolean gelesen) { this.gelesen = gelesen; }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", fileName='" + fileName + '\'' +
                ", base64Data='" + base64Data + '\'' +
                ", gelesen=" + gelesen +
                '}';
    }
}
