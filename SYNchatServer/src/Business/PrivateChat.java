package Business;

import Acquaintance.IChatHistory;
import Acquaintance.IPrivateChat;
import java.util.ArrayList;
import java.util.List;


public class PrivateChat implements IPrivateChat {
    
    private List<Integer> userIDs = new ArrayList<>();
    private int chatID = -1;
    private String name;
    private ChatHistory ch;
    
    
    /*
    PrivateChat(List<Integer> userIDs, int chatID, String name, IChatHistory ch) {
        this.userIDs = userIDs;
        this.chatID = chatID;
        this.name = name;
        this.ch = new ChatHistory(ch.getMsgLoadedCount(), ch.getMsgList());
    }
    */
    

    @Override
    public List<Integer> getUserIDs() {
        return this.userIDs;
    }

    @Override
    public int getChatID() {
        return this.chatID;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IChatHistory getCh() {
        return this.ch;
    }

}
