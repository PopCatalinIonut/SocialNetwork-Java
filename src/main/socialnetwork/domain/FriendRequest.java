package main.socialnetwork.domain;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FriendRequest extends Entity<Long> {

    private Long from;
    private Long to;
    private String status;
    private LocalDateTime date;
    private static long lastId=0;

    public Long getFrom() { return from; }

    public Long getTo() { return to; }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }

    public LocalDateTime getDate() { return date; }

    public FriendRequest(Long from, Long to, String status, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.date = date;
        setId(++lastId);
    }

    @Override
    public String toString() {
        return getId()+" ; " +from+" -> "+ to+" : " + status ;
    }
}
