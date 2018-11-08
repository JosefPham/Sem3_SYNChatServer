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

    @Override
    public void updateTimestamp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
