package Connection;

import Acquaintance.IFriends;
import Acquaintance.IProfile;
import Acquaintance.IUser;
import Acquaintance.Nationality;
import java.util.Map;

public class ConUser implements IUser {

    private int userID;
    private boolean banned; // a flag for if the user is banned
    private int reports;    // the amount of reprts a user have recived
    private Map<Integer, String> chats;
    private IFriends friends;
    private IProfile profile;

    public ConUser(String firstName, String lastName, Nationality nationality, String profileText) {
        profile = new ConProfile(firstName, lastName, nationality, "");
    }

    public ConUser(int userID, boolean banned, int reports, Map<Integer, String> chats, IFriends friends, IProfile profile) {
        this.userID = userID;
        this.banned = banned;
        this.reports = reports;
        this.chats = chats;
        this.friends = friends;
        this.profile = profile;
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
    public Map<Integer, String> getChats() {
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

    public void setChats(Map<Integer, String> chats) {
        this.chats = chats;
    }

    public void setProfile(IProfile profile) {
        this.profile = profile;
    }

    @Override
    public IFriends getFriends() {
        return friends;
    }
    
    public void setFriends(IFriends friends) {
        this.friends = friends;
    }
}
