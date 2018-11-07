package Connection;

import Acquaintance.IProfile;
import Acquaintance.IUser;
import Acquaintance.Nationality;
import java.util.List;

public class ConUser implements IUser {

    private int userID;
    private boolean banned; // a flag for if the user is banned
    private int reports;    // the amount of reprts a user have recived
    private List<Integer> chats;
    private IProfile profile;

    public ConUser(String firstName, String lastName, Nationality nationality, String profileText) {
        profile = new ConProfile(firstName, lastName, nationality, "");
    }

    public ConUser(int userID, boolean banned, int reports, List<Integer> chats, IProfile profile) {
        this.userID = userID;
        this.banned = banned;
        this.reports = reports;
        this.chats = chats;
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
    public List<Integer> getChats() {
        return chats;
    }

    @Override
    public IProfile getProfile() {
        return profile;
    }
}
