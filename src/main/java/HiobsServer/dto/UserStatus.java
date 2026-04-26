package HiobsServer.dto;

/**
 * Den 20.03.2026
 */

/**
 * Online Status von User
 * zugesendet von hiobsCliest/stompConnect(){
 *          ...
 *          stompClient.send("/app/user.online", {}, JSON.stringify({ userId: meineId }));
 *          Zeile: 25
 *          ... weiter angaben
 *          }
 */
public class UserStatus {
    private String userId;
    private boolean online;

    // Getter & Setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }
}
