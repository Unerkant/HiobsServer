package HiobsServer.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Den 20.11.2024
 */

@Entity
public class Usern {

    @Id
    @GeneratedValue
    private long id;
    private String bild;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private String datum;
    private String email;
    private String other;
    private String passwort;
    private String pseudonym;
    private String role;
    private String sprache;
    private String telefon;
    private String token;
    private String username;
    private String uservorname;

    public Usern(){
        //Leer
    }

    public Usern(Long id, String bild, String datum, String email, String other,
                 String passwort, String pseudonym, String role, String sprache,
                 String telefon, String token, String username, String uservorname ) {
        this.id         = id;
        this.bild       = bild;
        this.datum      = datum;
        this.email      = email;
        this.other      = other;
        this.passwort   = passwort;
        this.pseudonym  = pseudonym;
        this.role       = role;
        this.sprache    = sprache;
        this.telefon    = telefon;
        this.token      = token;
        this.username   = username;
        this.uservorname= uservorname;

    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getBild() { return bild; }
    public void setBild(String bild) { this.bild = bild; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public String getPseudonym() { return pseudonym; }
    public void setPseudonym(String pseudonym) { this.pseudonym = pseudonym; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSprache() { return sprache; }
    public void setSprache(String sprache) { this.sprache = sprache; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUservorname() { return uservorname; }
    public void setUservorname(String uservorname) { this.uservorname = uservorname; }


    @Override
    public String toString() {
        return "Usern: {" +
                "  id=" + id +
                ", bild='" + bild + '\'' +
                ", datum='" + datum + '\'' +
                ", email='" + email + '\'' +
                ", other='" + other + '\'' +
                ", passwort='" + passwort + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", role='" + role + '\'' +
                ", sprache='" + sprache + '\'' +
                ", telefon='" + telefon + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", uservorname='" + uservorname + '\'' +
                '}';
    }
}
