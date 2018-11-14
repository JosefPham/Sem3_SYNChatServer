package Business;

import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IProfile;
import Acquaintance.IUser;
import Acquaintance.Nationality;
import java.util.HashMap;
import java.util.Map;


public class ServerSystem {

    private static ServerSystem instance = null;

    private Map<Integer,User> onlineUsers;

    private ServerSystem() {
        onlineUsers = new HashMap();
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
            System.out.println("DBlog: " + DBlog);
            System.out.println(DBlog.getUser().getUserID());
            Friends friends = new Friends(DBlog.getUser().getFriends().getFriendlist());
            User onlineUser = new User(DBlog.getUser().getProfile().getFirstName(), DBlog.getUser().getProfile().getLastName(), DBlog.getUser().getProfile().getNationality(), DBlog.getUser().getProfile().getProfileText());
            onlineUser.setUserID(DBlog.getUser().getUserID());
            onlineUser.setProfile(DBlog.getUser().getProfile());
            onlineUser.setReports(DBlog.getUser().getReports());
            onlineUser.setBanned(DBlog.getUser().isBanned());
            onlineUser.setChats(DBlog.getUser().getChats());
            onlineUser.setFriends(friends);
            onlineUsers.put(onlineUser.getUserID(), onlineUser);
            // add user to server
            // maybe check stuff? Does the user have any chats?
        }

        return DBlog;
    }


    protected int changeInfo(IManagement management) {
        int returnstatus = 0;
        IManagement tmpManagement = new Management(management.getAction(), management.getUserID(), management.getPw(), management.getString1());
        //Action 1 = changeMail
        //Action 2 = changePw
        if (tmpManagement.getAction() == 1) {
            returnstatus = BusinessFacade.getInstance().updateMailSQL(management);
        } else if (tmpManagement.getAction() == 2) {
            returnstatus = BusinessFacade.getInstance().updatePwSQL(management);
        }
        return returnstatus;
    }

    protected boolean updateProfile(IUser user) {
        IUser oldUser = onlineUsers.get(user.getUserID());
        
        if(!oldUser.getProfile().getFirstName().equals(user.getProfile().getFirstName())){
            return BusinessFacade.getInstance().alterProfile(user);
        }
        if(!oldUser.getProfile().getLastName().equals(user.getProfile().getLastName())){
            return BusinessFacade.getInstance().alterProfile(user);
        }
        if(!oldUser.getProfile().getProfileText().equals(user.getProfile().getProfileText())){
            return BusinessFacade.getInstance().alterProfile(user);
        }
        if(!oldUser.getProfile().getNationality().equals(user.getProfile().getNationality())){
            return BusinessFacade.getInstance().alterProfile(user);
        }
        
        return false;
        
    }

    User getUser(int userID){
        return onlineUsers.get(userID);
    }

     Map<Integer, User> getOnlineUsers() {
        return onlineUsers;
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
