package HiobsServer.admin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Den 30.10.2024
 */

@Entity
public class Admin {

    @Id
    @GeneratedValue
    private long id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private String datum;
    private String email;
    private String other;
    private String passwort;
    private String pseudonym;
    private String role;
    private String telefon;
    private String username;
    private String uservorname;

    public Admin(){
        //Leer
    }

    private Admin(long id, String datum, String email, String other, String passwort, String pseudonym,
                  String role,String telefon, String username, String uservorname ){
        this.id             = id;
        this.datum          = datum;
        this.email          = email;
        this.other          = other;
        this.passwort       = passwort;
        this.pseudonym      = pseudonym;
        this.role           = role;
        this.telefon        = telefon;
        this.username       = username;
        this.uservorname    = uservorname;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

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

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUservorname() { return uservorname; }
    public void setUservorname(String uservorname) { this.uservorname = uservorname; }

    @Override
    public String toString() {
        return "Admin{" +
                "  id=" + id +
                ", datum='" + datum + '\'' +
                ", email='" + email + '\'' +
                ", other='" + other + '\'' +
                ", passwort='" + passwort + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", role='" + role + '\'' +
                ", telefon='" + telefon + '\'' +
                ", username='" + username + '\'' +
                ", uservorname='" + uservorname + '\'' +
                '}';
    }
}
