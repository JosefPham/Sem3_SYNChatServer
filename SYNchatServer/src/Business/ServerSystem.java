package Business;

import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ServerSystem {

    private static ServerSystem instance = null;
    private Map<Integer,User> onlineUsers;
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

    ILogin login(ILogin log) {
        ILogin login = new Login(log.gethMail(), log.gethPW());

        ILogin DBlog = BusinessFacade.getInstance().checkLoginDB(login);

        if (DBlog.getLoginvalue() == 2) {
            Friends friends = new Friends(DBlog.getUser().getFriends().getFriendlist());
            User onlineUser = new User(DBlog.getUser().getProfile().getFirstName(), DBlog.getUser().getProfile().getLastName(), DBlog.getUser().getProfile().getNationality(), DBlog.getUser().getProfile().getProfileText());
            onlineUser.setUserID(DBlog.getUser().getUserID());
            onlineUser.setProfile(DBlog.getUser().getProfile());
            onlineUser.getProfile().setPicture(DBlog.getUser().getProfile().getPicture());
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
    
    
    public void removeOnlineUser(int userID){
        onlineUsers.remove(userID);
        
    }


    protected int changeInfo(IManagement management, int userID) {
        int returnstatus = 0;
        IManagement tmpManagement = new Management(management.getAction(), management.getPw(), management.getString1());
        //Action 1 = changeMail
        //Action 2 = changePw
        if (tmpManagement.getAction() == 1) {
            returnstatus = BusinessFacade.getInstance().updateMailSQL(management, userID);
        } else if (tmpManagement.getAction() == 2) {
            returnstatus = BusinessFacade.getInstance().updatePwSQL(management, userID);
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
    
    IFriends updateFriends(IFriends friends, int userID) {
        if(!onlineUsers.containsKey(userID)) {
            System.out.println("User not found in onlineUsers!");
            return new Friends(new ArrayList<>());
        } else {
            return onlineUsers.get(userID).updateFriends(friends, userID);
        }
    }
    
    Map<Integer, User> getOnlineUsers() {
        return onlineUsers;
    }
    
    public synchronized Map updatePublicChatUsers(int userID){
        if(publicChatUser.containsKey(userID)){
            publicChatUser.remove(userID);
            return publicChatUser;
        } else{
            publicChatUser.put(userID, onlineUsers.get(userID));
            return publicChatUser;
            // send tilbage til sigurd
        }
        
        
    }
    
}
