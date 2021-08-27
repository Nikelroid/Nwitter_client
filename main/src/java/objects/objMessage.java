package objects;

public class objMessage {

    public String text;
    public String sender;
    public String receiver;
    public boolean seen;
    public boolean delivered;
    public boolean sent;
    public String time;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getReceiver() {
        return receiver;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public objMessage(int id,String text, String sender,
                     String reciver,String time, boolean seen,
                      boolean delivered,boolean sent) {

        this.id=id;
        this.text = text;
        this.sender = sender;
        this.receiver =reciver;
        this.seen=seen;
        this.time=time;
        this.delivered=delivered;
        this.sent=sent;

    }
}
