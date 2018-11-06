package Connection;

/**
 *
 * @author Group 9
 */
public class ConTextMessage extends ConMessage {
        String msg;
    
    public ConTextMessage(int senderID, String msg) {
        super(senderID);
        this.msg = msg;
    }  
}
