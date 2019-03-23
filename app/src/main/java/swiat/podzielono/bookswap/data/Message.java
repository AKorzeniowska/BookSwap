package swiat.podzielono.bookswap.data;

public class Message {

    private String message;
    private String user_name;
    private long timestamp;

    public Message() {}

    public Message(String message, String user_name, long timestamp) {
        this.message = message;
        this.user_name = user_name;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
