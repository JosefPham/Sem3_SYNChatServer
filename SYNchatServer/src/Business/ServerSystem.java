package Business;

import Acquaintance.IFriends;
import Acquaintance.ILogin;
import java.util.HashMap;
import java.util.Map;


public class ServerSystem {
    
    private static ServerSystem instance = null;
    private Map<Integer,User> onlineUsers = new HashMap<>();
    
    private ServerSystem(){
    }
    
    static ServerSystem getInstance() {
        if (instance == null) {
            instance = new ServerSystem();
        }
        return instance;
    }

    ILogin login(ILogin log) {
        ILogin login = new Login(log.gethMail(), log.gethPW());
        
        ILogin DBlog = BusinessFacade.getInstance().checkLoginDB(login);
        
        if (DBlog.getLoginvalue() == 2) {
            // add user to server
            // maybe check stuff? Does the user have any chats?
        }
        
        return DBlog;
    }

    Boolean updateFriends(IFriends friends, int userID) {
        if(!onlineUsers.containsKey(userID)) {
            System.out.println("User not found in onlineUsers!");
        } else {
            return onlineUsers.get(userID).updateFriends(friends, userID);
        }
        return false;
    }
    
}
