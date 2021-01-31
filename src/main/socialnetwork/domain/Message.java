package main.socialnetwork.domain;

import main.socialnetwork.utils.Constants;
import java.time.LocalDateTime;
import java.util.List;


public class Message extends Entity<Long> {
    private Long from;
    private List<Long> to;
    private LocalDateTime date;
    private String message;
    private static long lastId=0;
    private Long reply;
    public Long getReply() { return reply; }


    public void setReply(Long reply) { this.reply = reply; }

    @Override
    public String toString() {
       return getDate().format(Constants.DATE_TIME_FORMATTER)+": "+ getFrom()+" : "+getMessage();
    }

    public boolean inTo(Long Id){ return to.contains(Id); }
    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Message(Long from, List<Long> to,String message, LocalDateTime date) {
        this.setId(++lastId);
        this.from = from;
        this.to = to;
        this.date = date;
        this.message = message;
        this.reply=null;
    }
    public Message(Long from, List<Long> to,String message, LocalDateTime date,Long reply) {
        this.setId(++lastId);
        this.from = from;
        this.to = to;
        this.date = date;
        this.message = message;
        this.reply=reply;
    }
}
