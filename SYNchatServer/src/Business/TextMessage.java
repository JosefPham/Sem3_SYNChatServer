/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Acquaintance.ITextMessage;

/**
 *
 * @author Sigurd E. Espersen
 */
public class TextMessage extends Message implements ITextMessage {
    
    private String msg;
    
    public TextMessage(int senderID, String msg) {
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
