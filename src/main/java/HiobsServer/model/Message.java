package HiobsServer.model;

/**
 * Den 15.09.2024
 */

public class Message {

    private String name;
    private String text;
    private String recipient;

    public Message(){}

    public Message(String name, String text, String recipient) {
        this.name       = name;
        this.text       = text;
        this.recipient  = recipient;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }


    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }

}
