package Business;

import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IUser;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Group 9
 */
public class ServerSystem {

    private static ServerSystem instance = null;
    private Map<Integer, User> onlineUsers;
    private Map<Integer, IUser> publicChatUser;

    private ServerSystem() {
        onlineUsers = new HashMap();
        publicChatUser = new HashMap<>();
    }

    static ServerSystem getInstance() {
        if (instance == null) {
            instance = new ServerSystem();
        }
        return instance;
    }

    synchronized ILogin login(ILogin log) {
        ILogin login = new Login(log.gethMail(), log.gethPW());

        ILogin DBlog = BusinessFacade.getInstance().checkLoginDB(login);

        if (DBlog.getLoginvalue() == 2) {
            User onlineUser = new User(DBlog.getUser());
            onlineUsers.put(DBlog.getUser().getUserID(), onlineUser);
            // add user to server
            // maybe check stuff? Does the user have any chats?
        }

        return DBlog;
    }

    public synchronized void removeOnlineUser(int userID) {
        onlineUsers.remove(userID);

    }

    protected synchronized boolean changeInfo(IManagement management, int userID) {
        boolean returnstatus = false;
        if ((management.getAction() == 0) || (management.getAction() == 1)) {
            returnstatus = BusinessFacade.getInstance().verify(management, userID);
        } else if (management.getAction() == 2) {
            returnstatus = BusinessFacade.getInstance().alterProfile(management, userID);
            if (!management.getMail().equals("")) {
                returnstatus = BusinessFacade.getInstance().updateMailSQL(management, userID);
            } else if (!management.getPw().equals("")) {
                returnstatus = BusinessFacade.getInstance().updatePwSQL(management, userID);
            }
        }
        return returnstatus;

        //0 check pw
        //1 check mail
        //2 update all
    }

    protected synchronized boolean updateProfile(IManagement management, int userID) {
        boolean status = false;
        return BusinessFacade.getInstance().alterProfile(management, userID);

    }

    synchronized boolean updateFriends(IFriends friends, int userID) {
        if (!onlineUsers.containsKey(userID)) {
            System.out.println("User not found in onlineUsers!");
            return false;
        } else {
            return onlineUsers.get(userID).updateFriends(friends, userID);
        }
    }

    synchronized Map<Integer, User> getOnlineUsers() {
        return onlineUsers;
    }

    public synchronized Map updatePublicChatUsers(int userID) {
        if (publicChatUser.containsKey(userID)) {
            publicChatUser.remove(userID);
            return publicChatUser;
        } else {
            publicChatUser.put(userID, onlineUsers.get(userID));
            System.out.println("Mappet i serverSYs: " + publicChatUser.entrySet());
            return publicChatUser;
        }
    }
}
