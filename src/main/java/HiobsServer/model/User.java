package HiobsServer.model;

import jakarta.persistence.Transient;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Den 19.12.2025
 * Umstieg auf Database: MongoDB
 */

@Document(collection = "users") // Definiert die Collection in MongoDB
public class User {

    @Id
    private String serverId;
    private Instant datum = Instant.now();

    @Indexed(unique = true)
    private String username;
    private String uservorname;
    private String pseudonym;

    @Indexed(unique = true)
    private String usermail;
    private String password; // Für spätere Nutzung
    private String telefon;

    private List<String> roles = new ArrayList<>();
    private List<String> friendIds = new ArrayList<>();

    private boolean active = true;
    private Long sperrdatum; // Millisekunden für die Sperre
    private String profilePicture;
    private Instant lastLogin; // Zeitpunkt des letzten Zugriffs

    @Transient  // Bedeutet wird nicht für die Datenbank verwendet
    private String type;
    @Transient
    private String letzteNachricht;
    @Transient
    private Instant datumLetzteNachricht;
    @Transient
    private boolean gelesen = false;
    @Transient
    private Long unreadCount;


    public User() {}


    public User(String serverId, Instant datum, String username, String uservorname, String pseudonym, String usermail,
                String password, String telefon, List<String> roles, List<String> friendIds, boolean active,
                Long sperrdatum, String profilePicture, Instant lastLogin, String type, String letzteNachricht,
                Instant datumLetzteNachricht, boolean gelesen, Long unreadCount ) {
        this.serverId = serverId;
        this.datum = datum;
        this.username = username;
        this.uservorname = uservorname;
        this.pseudonym = pseudonym;
        this.usermail = usermail;
        this.password = password;
        this.telefon = telefon;
        this.roles = roles;
        this.friendIds = friendIds;
        this.active = active;
        this.sperrdatum = sperrdatum;
        this.profilePicture = profilePicture;
        this.lastLogin = lastLogin;
        this.type = type;
        this.letzteNachricht = letzteNachricht;
        this.datumLetzteNachricht = datumLetzteNachricht;
        this.gelesen = gelesen;
        this.unreadCount = unreadCount;
    }

    // Getter und Setter


    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }

    public Instant getDatum() { return datum; }
    public void setDatum(Instant datum) { this.datum = datum; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUservorname() { return uservorname; }
    public void setUservorname(String uservorname) { this.uservorname = uservorname; }

    public String getPseudonym() { return pseudonym; }
    public void setPseudonym(String pseudonym) {  this.pseudonym = pseudonym; }

    public String getUsermail() { return usermail; }
    public void setUsermail(String usermail) { this.usermail = usermail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<String> getFriendIds() { return friendIds; }
    public void setFriendIds(List<String> friendIds) { this.friendIds = friendIds; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Long getSperrdatum() { return sperrdatum; }
    public void setSperrdatum(Long sperrdatum) { this.sperrdatum = sperrdatum; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLetzteNachricht() { return letzteNachricht; }
    public void setLetzteNachricht(String letzteNachricht) { this.letzteNachricht = letzteNachricht; }

    public Instant getDatumLetzteNachricht() { return datumLetzteNachricht; }
    public void setDatumLetzteNachricht(Instant datumLetzteNachricht) {
        this.datumLetzteNachricht = datumLetzteNachricht;
    }

    public boolean isGelesen() { return gelesen;}
    public void setGelesen(boolean gelesen) { this.gelesen = gelesen; }

    public Long getUnreadCount() { return unreadCount; }
    public void setUnreadCount(Long unreadCount) { this.unreadCount = unreadCount; }


    // String
    @Override
    public String toString() {
        return "User{" +
                "serverId='" + serverId + '\'' +
                ", datum=" + datum +
                ", username='" + username + '\'' +
                ", uservorname='" + uservorname + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", usermail='" + usermail + '\'' +
                ", telefon='" + telefon + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", friendIds=" + friendIds +
                ", active=" + active +
                ", sperrdatum=" + sperrdatum +
                ", profilePicture='" + profilePicture + '\'' +
                ", lastLogin=" + lastLogin +
                ", type='" + type + '\'' +
                ", letzteNachricht='" + letzteNachricht + '\'' +
                ", datumLetzteNachricht='" + datumLetzteNachricht + '\'' +
                ", gelesen=" + gelesen +
                ", unreadCount=" + unreadCount +
                '}';
    }
}
