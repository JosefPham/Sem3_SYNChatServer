package Business;

import Acquaintance.IPrivateChat;
import Acquaintance.IProfile;
import Acquaintance.IUser;
import Acquaintance.Nationality;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements IUser {

    private int userID;
    private boolean banned; // a flag for if the user is banned
    private int reports;    // the amount of reprts a user have recived
    private List<Integer> chats;
    private IProfile profile;
    private Map<Integer, IPrivateChat> activePrivateChats = new HashMap<>();

    public User(String firstName, String lastName, Nationality nationality, String profileText) {
        new Profile(firstName, lastName, nationality, "");
    }

    public User(int userID, boolean banned, int reports, List<Integer> chats) {
        this.userID = userID;
        this.banned = banned;
        this.reports = reports;
        this.chats = chats;
    }

    @Override
    public int getUserID() {
        return userID;
    }

    @Override
    public boolean isBanned() {
        return banned;
    }

    @Override
    public int getReports() {
        return reports;
    }

    @Override
    public List<Integer> getChats() {
        return chats;
    }

    @Override
    public IProfile getProfile() {
        return profile;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setReports(int reports) {
        this.reports = reports;
    }

    public void setChats(List<Integer> chats) {
        this.chats = chats;
    }

    public void setProfile(IProfile profile) {
        this.profile = profile;
    }

    // skal muligvis bruges på client siden
//    @Override
//    public boolean loadPrivateChat(int chatID) {
//        if (activePrivateChats.containsKey(chatID)) {
//            return true;
//        }
//        IPrivateChat putChat = BusinessFacade.getInstance().loadPrivateChat(chatID);
//        if(putChat != null){
//        activePrivateChats.put(userID, putChat);
//        return true;
//        }
//        return false;
//    }
//    
//    IPrivateChat getActivePrivateChat(int chatID){
//        if(loadPrivateChat(chatID)){
//           return activePrivateChats.get(chatID);
//        }
//        throw new IllegalArgumentException("the chatID was not found in the database, maby");
//    }
    
    IPrivateChat handlePrivateChat(IPrivateChat prichat){
        //validere på chat id er det -1 skal chatten oprettes, ellers er det en besked som skal tilføjes
        IPrivateChat realTimeChat = new 
        if(prichat.getChatID() == -1){
            IPrivateChat newchat = BusinessFacade.getInstance().createNewPrivateChat(prichat);  // 
                      
            // chek if other user is online, if thay are the chat id is added to thire chats list
            for(Integer userID : newchat.getUserIDs()){
                if(ServerSystem.getInstance().getOnlineUsers().containsKey(userID)){
                    ServerSystem.getInstance().getUser(userID).addChat(newchat.getChatID());
                }
                else {
                    newchat.getUserIDs().remove(userID);    // removes users from the list that is not online, the clienthandler sends out the meesage to all users still in the list.
                }
                return newchat;
            }
        }
        
// Tilføj besked:

        //tilføj msg til sql
        //finde rigtige private chat.
        //tilføj msg til private chat på server.
        //finde modtagere som er online
        //opret nyt IPC med msg og modtagere som er online
        // returner nye IPC
        return null;
    }

    private void addChat(int chatID) {
        chats.add(chatID);
    }
}
