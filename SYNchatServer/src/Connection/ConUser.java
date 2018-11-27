package Connection;

import Acquaintance.IFriends;
import Acquaintance.IProfile;
import Acquaintance.IUser;
import java.util.List;

public class ConUser implements IUser {

    private int userID;
    private boolean banned; // a flag for if the user is banned
    private int reports;    // the amount of reprts a user have recived
    private List<Integer> chats;
    private IFriends friends;
    private IProfile profile;

    public ConUser(IUser user) {
        if (user != null) {
            this.profile = new ConProfile(user.getProfile());
            this.userID = user.getUserID();
            this.banned = user.isBanned();
            this.reports = user.getReports();
            this.chats = user.getChats();
            this.friends = new ConFriends(user.getFriends());
        }
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

    @Override
    public IFriends getFriends() {
        return friends;
    }

    public void setFriends(IFriends friends) {
        this.friends = friends;
    }
}
