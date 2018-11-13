package Acquaintance;

import java.io.Serializable;
import java.util.List;

public interface IUser extends Serializable {

    int getUserID();

    boolean isBanned();

    int getReports();

    List<Integer> getChats();

    IProfile getProfile();

    IFriends getFriends();
}
