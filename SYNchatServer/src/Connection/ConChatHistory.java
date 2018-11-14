package Connection;

import Acquaintance.IChatHistory;
import Acquaintance.IMessage;
import Acquaintance.ITextMessage;
import java.util.ArrayList;
import java.util.List;


public class ConChatHistory implements IChatHistory{
    
    private int msgLoadedCount;
    List<IMessage> msgList;
    
    
    ConChatHistory(int msgLoadedCount, List<IMessage> msgList) {
        this.msgLoadedCount = msgLoadedCount;
        msgList = new ArrayList<>();
        for(IMessage msg : msgList){
        if(msg instanceof ITextMessage){    // Remember to add an if for additional message types when theay are added to the system
            msgList.add(new ConTextMessage(msg.getSenderID(), msg.getContext()));
        }
    }
    }

    @Override
    public int loadMoreMessages() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMsgLoadedCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<IMessage> getMsgList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
