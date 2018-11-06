package Connection;

import Acquaintance.IMessage;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Group 9
 */
public abstract class ConMessage implements Serializable, IMessage {

    int senderID;
    Date timestamp;

    public ConMessage(int senderID) {
        this.senderID = senderID;
        timestamp = new Date();
    }

    @Override
    public int getSenderID() {
        return senderID;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }
}
