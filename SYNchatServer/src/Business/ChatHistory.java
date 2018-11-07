package Business;

import Acquaintance.IChatHistory;
import Acquaintance.IMessage;
import java.util.ArrayList;
import java.util.List;

public class ChatHistory implements IChatHistory {
    
    private int msgLoadedCount;
    private List<IMessage> msgList;
    
    
    /*
    public ChatHistory(int msgLoadedCount, List<IMessage> msgList) {
        this.msgLoadedCount = msgLoadedCount;
        this.msgList = new ArrayList<>();
        for(IMessage ms: msgList){
            this.msgList.add(ms);
        }
    }
    */
    

    @Override
    public int getMsgLoadedCount() {
        return this.msgLoadedCount;
    }

    @Override
    public List<IMessage> getMsgList() {
        return this.msgList;
    }
    
    
    @Override
    public int loadMoreMessages() {
        throw new UnsupportedOperationException("For implementation in a later version.");
    }
}