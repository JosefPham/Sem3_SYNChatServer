package Persistence;

import java.sql.Timestamp;


public class PerTextMessage extends PerMessage {
    
    private String text;
    
    PerTextMessage(int senderID, Timestamp time, String text) {
        super(senderID, time);
        this.text = text;
    }
    
    public void setMsg(String text) {
        this.text = text;
    }
    
    public String getContext() {
        return this.text;
    }
}