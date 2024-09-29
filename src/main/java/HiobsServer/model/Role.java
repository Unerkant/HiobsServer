package HiobsServer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Den 15.09.2024
 */

@Entity
public class Role {

    @Id
    @GeneratedValue
    private long id;
    private String authority;

    public Role(){}
    public Role(long id, String authority){
        this.id         = id;
        this.authority  = authority;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAuthority() { return authority; }
    public void setAuthority(String authority) { this.authority = authority; }

    @Override
    public String toString() {
        return "Role{" +
                "  id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }

}
