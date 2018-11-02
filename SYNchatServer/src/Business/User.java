package Business;

import Acquaintance.IFriends;
import Acquaintance.IUser;
import java.util.List;

public class User implements IUser {

    private int userID;
    private String tmpName; //must be removed when profile is implemented
    private boolean banned; // a flag for if the user is banned
    private int reports;    // the amount of reprts a user have recived
    private List<Integer> chats;
    private Friends friends;

    public User(String tmpName) {
        this.tmpName = tmpName;
    }

    public User(int userID, String tmpName, boolean banned, int reports, List<Integer> chats, Friends newFriends) {
        this.userID = userID;
        this.tmpName = tmpName;
        this.banned = banned;
        this.reports = reports;
        this.chats = chats;
        this.friends = newFriends;
    }

    @Override
    public int getUserID() {
        return userID;
    }

    @Override
    public String getTmpName() {
        return tmpName;
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

    Boolean updateFriends(IFriends newFriends, int userID) {
        return friends.updateFriends(newFriends, userID);
    }

}
