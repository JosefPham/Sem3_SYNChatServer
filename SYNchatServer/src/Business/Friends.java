package Business;

import Acquaintance.IFriends;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Group 9
 */
public class Friends implements IFriends {

    List<Integer> friendList = new ArrayList<>();

    public Friends(IFriends friends) {
        this.friendList = friends.getFriendlist();
    }

    public Friends(List<Integer> friendlist) {
        for (Integer key : friendlist) {
            this.friendList.add(key);
        }
    }

    @Override
    public List<Integer> getFriendlist() {
        return friendList;
    }

    int updateFriends(IFriends newFriends, int userID) {
        if (this.friendList.size() < newFriends.getFriendlist().size()) {
            for (Integer newID : newFriends.getFriendlist()) {
                if (!this.friendList.contains(newID)) {
                    this.friendList.add(newID);

                    BusinessFacade.getInstance().addFriend(userID, (int) newID);
                    return newID;
                }
            }
        } else {
            for (Integer ID : this.friendList) {
                if (!newFriends.getFriendlist().contains(ID)) {
                    this.friendList.remove(ID);
                    BusinessFacade.getInstance().removeFriend(userID, (int) ID);
                    return ID;
                }
            }
        }
        return -1;
    }

    public boolean addFriend(int userID) {
        friendList.add(userID);
        return true;
    }

    public void removeFriend(int userID) {
        friendList.remove(userID);
    }

}
