package HiobsServer.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Den 5.12.2024
 */

@Entity
public class Exception {

    @Id
    @GeneratedValue
    private long id;
    private int count;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private String datum;
    private int errcode;
    private String errip;
    private String errquelle;
    private String errtext;
    private String other;
    private String role;

    public Exception(){}

    public Exception(Long id, int count, String datum, int errcode, String errip, String errquelle,
                     String errtext, String other, String role) {
        this.id         = id;
        this.count      = count;
        this.datum      = datum;
        this.errcode    = errcode;
        this.errip      = errip;
        this.errquelle  = errquelle;
        this.errtext    = errtext;
        this.other      = other;
        this.role       = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public int getErrcode() { return errcode; }
    public void setErrcode(int errcode) { this.errcode = errcode; }

    public String getErrip() { return errip; }
    public void setErrip(String errip) { this.errip = errip; }

    public String getErrquelle() { return errquelle; }
    public void setErrquelle(String errquelle) { this.errquelle = errquelle; }

    public String getErrtext() { return errtext; }
    public void setErrtext(String errtext) { this.errtext = errtext; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }


    @Override
    public String toString() {
        return "Exceptions{" +
                "  id=" + id +
                "  count=" + count +
                ", datum='" + datum + '\'' +
                ", errcode=" + errcode +
                ", errip='" + errip + '\'' +
                ", errquelle='" + errquelle + '\'' +
                ", errtext='" + errtext + '\'' +
                ", other='" + other + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
