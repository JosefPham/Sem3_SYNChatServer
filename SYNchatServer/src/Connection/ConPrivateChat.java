package Connection;

import Acquaintance.IChatHistory;
import Acquaintance.IPrivateChat;
import java.util.ArrayList;
import java.util.List;


public class ConPrivateChat implements IPrivateChat{

    private List<Integer> userIDs = new ArrayList<>();
    private int chatID = -1;
    private String name;
    private ConChatHistory ch;
    
    
    
     ConPrivateChat(List<Integer> userIDs, int chatID, String name, IChatHistory ch) {
        this.userIDs = userIDs;
        this.chatID = chatID;
        this.name = name;
        this.ch = new ConChatHistory(ch.getMsgLoadedCount(), ch.getMsgList());
    }
    @Override
    public List<Integer> getUserIDs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChatID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IChatHistory getCh() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
