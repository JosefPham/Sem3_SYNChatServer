package Business;

import Acquaintance.IMessage;
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
    /*
    @Override
    public boolean loadPrivateChat(int chatID) {
        if (activePrivateChats.containsKey(chatID)) {
            return true;
        }
        IPrivateChat putChat = BusinessFacade.getInstance().loadPrivateChat(chatID);
        if(putChat != null){
        activePrivateChats.put(userID, putChat);
        return true;
        }
        return false;
    }
     */
    // skal muligvis bruges på client siden
    /*
    IPrivateChat getActivePrivateChat(int chatID){
        if(loadPrivateChat(chatID)){
           return activePrivateChats.get(chatID);
        }
        throw new IllegalArgumentException("the chatID was not found in the database (probably)");
    }
     */
    IPrivateChat handlePrivateChat(IPrivateChat prichat) {

        // update timestamp
        for (IMessage msg : prichat.getCh().getMsgList()) {
            msg.updateTimestamp();
        }

        //IPrivateChat realTimeChat = new PrivateChat(prichat.getUserIDs(), prichat.getChatID(), prichat.getName(), prichat.getCh());
        //validate chatID - if -1, new chat is created in db, otherwise add to existing chat
        IPrivateChat newchat;
        if (prichat.getChatID() == -1) {
            newchat = BusinessFacade.getInstance().createNewPrivateChat(prichat);  // IMPLEMENT in business/db! - save to db as a new chat
        } else {
            newchat = BusinessFacade.getInstance().addToPrivateChat(prichat);   // IMPLEMENT in business/db! - save new message to existing chat in db
        }

        // check if other user is online, if they are the chatID is added to their chats list
        for (Integer userID : newchat.getUserIDs()) {
            if (ServerSystem.getInstance().getOnlineUsers().containsKey(userID)) {
                if (prichat.getChatID() == -1) {
                    ServerSystem.getInstance().getUser(userID).addChat(newchat.getChatID());
                }
            } else {
                newchat.getUserIDs().remove(userID);    // remove users from the list if they are offline, the clienthandler will send out the meesage to all users still in the list.
            }
            return newchat;     // ********* måske skal det ikke altid være newchat der returnes - det er den vi henter fra databasen
        }

        return null;
    }

    private void addChat(int chatID) {
        chats.add(chatID);
    }
}
