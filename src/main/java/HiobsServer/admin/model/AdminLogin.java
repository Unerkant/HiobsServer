package HiobsServer.admin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

/**
 * Den 25.04.2025
 */

@Entity(name = "adminlogin")
public class AdminLogin {

    @Id
    @GeneratedValue
    private long id;
    private String browser;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private String datum;
    private String ip;
    private String land;
    private String name;
    private String other;
    private String plattform;
    private String role;
    private String standort;
    //@Column(columnDefinition="TEXT")
    @Lob
    private String text;

    public AdminLogin(){
        //nicht machen
    }

    public AdminLogin(long id, String browser, String datum, String ip, String land, String name,
                      String other, String plattform, String role, String standort, String text) {
        this.id = id;
        this.browser = browser;
        this.datum = datum;
        this.ip = ip;
        this.land = land;
        this.name = name;
        this.other = other;
        this.plattform = plattform;
        this.role = role;
        this.standort = standort;
        this.text = text;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getLand() { return land; }
    public void setLand(String land) { this.land = land; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }

    public String getPlattform() { return plattform; }
    public void setPlattform(String plattform) { this.plattform = plattform; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStandort() { return standort; }
    public void setStandort(String standort) { this.standort = standort; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    @Override
    public String toString() {
        return "Anmeldung{" +
                "id=" + id +
                ", browser='" + browser + '\'' +
                ", datum='" + datum + '\'' +
                ", ip='" + ip + '\'' +
                ", land='" + land + '\'' +
                ", name='" + name + '\'' +
                ", other='" + other + '\'' +
                ", plattform='" + plattform + '\'' +
                ", role='" + role + '\'' +
                ", standort='" + standort + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
