package lecture.domain;

import java.util.Date;

public class Notification {
    private String message;
    private Date timestamp;
    private User recipient;

    public Notification(String message, User recipient) {
        this.message = message;
        this.timestamp = new Date(); // sets current time as timestamp
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public User getRecipient() {
        return recipient;
    }
}
