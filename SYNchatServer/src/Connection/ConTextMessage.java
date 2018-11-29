package Connection;

import Acquaintance.ITextMessage;
import java.time.Instant;

/**
 *
 * @author Group 9
 */
public class ConTextMessage extends ConMessage implements ITextMessage {

    private String msg;

    public ConTextMessage(int senderID, String msg) {
        super(senderID);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String getContext() {
        return msg;
    }

    @Override
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
