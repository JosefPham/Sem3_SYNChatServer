package Persistence;

import Acquaintance.IChatHistory;
import Acquaintance.IPrivateChat;
import java.util.ArrayList;
import java.util.List;

public class PerPrivateChat implements IPrivateChat {
    
    private List<Integer> userIDs = new ArrayList<>();
    private int chatID = -1;
    private String name;
    private PerChatHistory ch;
    
    
    PerPrivateChat(List<Integer> userIDs, int chatID, String name, IChatHistory ch) {
        this.userIDs = userIDs;
        this.chatID = chatID;
        this.name = name;
        this.ch = new PerChatHistory(ch.getMsgLoadedCount(), ch.getMsgList());
    }
    
    @Override
    public List<Integer> getUserIDs() {
        return userIDs;
    }

    @Override
    public int getChatID() {
        return chatID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IChatHistory getCh() {
        return ch;
    }
}