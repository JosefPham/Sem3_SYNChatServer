package Connection;

/**
 *
 * @author Group 9
 */
public class ConTextMessage extends ConMessage {

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
}
