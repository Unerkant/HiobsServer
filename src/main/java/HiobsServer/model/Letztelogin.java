package HiobsServer.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

/**
 * Den 5.04.2025
 */

@Document(collection = "letztelogin")
public class Letztelogin {

    @Id
    @GeneratedValue
    private String id;
    private Instant datum;
    private String usertoken;
    private String usermail;
    private String pseudonym;
    private String appname;
    private String appvers;
    private String browser;
    private String ip;
    private String land;
    private String other;
    private String plattform;
    private String role;
    private String standort;
    private String text;


    public Letztelogin(){ /* leer */ }

    public Letztelogin(String id, Instant datum, String usertoken, String usermail, String pseudonym, String appname,
                       String appvers, String browser, String ip, String land, String other,
                       String plattform,  String role, String standort, String text ) {
        this.id = id;
        this.datum = datum;
        this.usertoken = usertoken;
        this.usermail = usermail;
        this.pseudonym = pseudonym;
        this.appname    = appname;
        this.appvers    = appvers;
        this.browser    = browser;
        this.ip         = ip;
        this.land       = land;
        this.other      = other;
        this.plattform  = plattform;
        this.role       = role;
        this.standort   = standort;
        this.text       = text;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Instant getDatum() { return datum; }
    public void setDatum(Instant datum) { this.datum = datum; }

    public String getUsertoken() { return usertoken; }
    public void setUsertoken(String usertoken) { this.usertoken = usertoken; }

    public String getUsermail() { return usermail; }
    public void setUsermail(String usermail) { this.usermail = usermail; }

    public String getPseudonym() { return pseudonym; }
    public void setPseudonym(String pseudonym) {  this.pseudonym = pseudonym; }

    public String getAppname() { return appname; }
    public void setAppname(String appname) { this.appname = appname; }

    public String getAppvers() { return appvers; }
    public void setAppvers(String appvers) { this.appvers = appvers; }

    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getLand() { return land; }
    public void setLand(String land) { this.land = land; }

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
        return "letzteLogin {" +
                "id=" + id +
                ", datum=" + datum +
                ", usertoken='" + usertoken + '\'' +
                ", usermail='" + usermail + '\'' +
                ", pseudonym='" + pseudonym + '\'' +
                ", appname='" + appname + '\'' +
                ", appvers='" + appvers + '\'' +
                ", browser='" + browser + '\'' +
                ", ip='" + ip + '\'' +
                ", land='" + land + '\'' +
                ", other='" + other + '\'' +
                ", plattform='" + plattform + '\'' +
                ", role='" + role + '\'' +
                ", standort='" + standort + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
