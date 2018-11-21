package Connection;

import Acquaintance.IMessage;
import java.io.Serializable;
import java.time.Instant;

/**
 *
 * @author Group 9
 */
public abstract class ConMessage implements Serializable, IMessage {

    int senderID;
    Instant timestamp;

    public ConMessage(int senderID) {
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
}
