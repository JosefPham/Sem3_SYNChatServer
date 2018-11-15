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

   synchronized ILogin login(ILogin log) {
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
    
    
    public synchronized void removeOnlineUser(int userID){
        onlineUsers.remove(userID);
        
    }


    protected synchronized boolean changeInfo(IManagement management, int userID) {
        boolean returnstatus = false;
        IManagement tmpManagement = new Management(management.getAction());
        if (tmpManagement.getAction() == 0) {
            returnstatus = BusinessFacade.getInstance().verify(management, userID);
        } else if (tmpManagement.getAction() == 1) {
            returnstatus = BusinessFacade.getInstance().verify(management, userID);
        }
        else if (tmpManagement.getAction() == 2){
            returnstatus = BusinessFacade.getInstance().alterProfile(onlineUsers.get(userID));
            if(!tmpManagement.getMail().isEmpty()){
                returnstatus = BusinessFacade.getInstance().updateMailSQL(management, userID);
            }
            else if(tmpManagement.getPw().isEmpty()){
               returnstatus = BusinessFacade.getInstance().updatePwSQL(management, userID);
            }
        }
        return returnstatus;
        
        
        //0 check pw
        //1 check mail
        //2 update all
    }

    protected synchronized boolean updateProfile(IUser user) {
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
    
    synchronized IFriends updateFriends(IFriends friends, int userID) {
        if(!onlineUsers.containsKey(userID)) {
            System.out.println("User not found in onlineUsers!");
            return new Friends(new ArrayList<>());
        } else {
            return onlineUsers.get(userID).updateFriends(friends, userID);
        }
    }
    
   synchronized Map<Integer, User> getOnlineUsers() {
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
