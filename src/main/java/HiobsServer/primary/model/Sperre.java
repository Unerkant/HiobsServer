package HiobsServer.primary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Den 16.01.2024
 */

@Entity
public class Sperre {

    @Id
    @GeneratedValue
    private long id;
    private int code;
    private int count;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private String datum;
    private String other;
    private Long sperrdatum;
    private String token;

    public Sperre(){
        // leer
    }

    public Sperre( Long id, int code, int count, String datum, String other, long sperrdatum, String token) {
        this.id         = id;
        this.code       = code;
        this.count      = count;
        this.datum      = datum;
        this.other      = other;
        this.sperrdatum = sperrdatum;
        this.token      = token;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String getDatum() { return datum; }
    public void setDatum(String datum) { this.datum = datum; }

    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }

    public Long getSperrdatum() { return sperrdatum; }
    public void setSperrdatum(Long sperrdatum) { this.sperrdatum = sperrdatum; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    @Override
    public String toString() {
        return "Sperre {" +
                "  id=" + id +
                ", count=" + count +
                ", code=" + code +
                ", datum='" + datum + '\'' +
                ", other=" + other +
                ", sperrdatum=" + sperrdatum +
                ", token=" + token +
                '}';
    }

}
