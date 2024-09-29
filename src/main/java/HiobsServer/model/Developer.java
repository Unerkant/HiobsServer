package HiobsServer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Set;

/**
 * Den 15.09.2024
 */

@Entity
public class Developer {

    @Id
    @GeneratedValue
    private long id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private String datum;
    private String username;
    private String uservorname;
    private String pseudonym;
    private String email;
    private String telefon;
    private String passwort;
    private String other;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "developer_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> role;
    /**
     *  ACHTUNG: die Tabelle users_roles wir von hibernate automatisch erstellt
     */

    public Developer(){}
    private Developer(long id, String datum, String username, String uservorname,
                      String pseudonym, String email, String telefon, String passwort, String other, Set<Role> role){
        this.id             = id;
        this.datum          = datum;
        this.username       = username;
        this.uservorname    = uservorname;
        this.pseudonym      = pseudonym;
        this.email          = email;
        this.telefon        = telefon;
        this.passwort       = passwort;
        this.other          = other;
        this.role           = role;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUservorname() { return uservorname; }
    public void setUservorname(String uservorname) { this.uservorname = uservorname; }

    public String getPseudonym() { return pseudonym; }
    public void setPseudonym(String pseudonym) { this.pseudonym = pseudonym; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getPasswort() { return passwort; }
    public void setPasswort(String passwort) { this.passwort = passwort; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }

    public Set<Role> getRole() { return role; }
    public void setRole(Set<Role> role) { this.role = role; }

    @Override
    public String toString() {
        return "Developer{" +
                "  id=" + id +
                ", datum='" + datum + '\'' +
                ", username='" + username + '\'' +
                ", uservorname='" + uservorname + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", passwort='" + passwort + '\'' +
                ", other='" + other + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
