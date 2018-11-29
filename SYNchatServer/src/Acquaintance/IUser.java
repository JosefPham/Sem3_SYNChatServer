package Acquaintance;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Group 9
 */
public interface IUser extends Serializable {

    int getUserID();

    boolean isBanned();

    int getReports();

    List<Integer> getChats();

    IProfile getProfile();

    IFriends getFriends();
}
