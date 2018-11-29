package Business;


import Acquaintance.IMessage;
import java.io.Serializable;
import java.time.Instant;
/**
 *
 * @author Group 9
 */
public abstract class Message implements Serializable, IMessage {

    int senderID;
    Instant timestamp;

    public Message(int senderID) {
        this.senderID = senderID;
        timestamp = Instant.now();
    }

    @Override
    public int getSenderID() {
        return senderID;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String getContext() {
        return "Abstract klasse getContext";
    }

    @Override
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
}
