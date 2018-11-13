/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Business;

import Acquaintance.IFriends;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Josef Pham
 */
public class Friends implements IFriends {

    Map<Integer,String> friendList = new HashMap<>();
    
    @Override
    public Map<Integer, String> getFriendlist() {
        return friendList;
    }
    
    boolean updateFriends(IFriends newFriends, int userID) {
        if(this.friendList.size() < newFriends.getFriendlist().size()) {
            for (Integer newID : newFriends.getFriendlist().keySet()) {
                if(!this.friendList.containsKey(newID)) {
                    this.friendList.put(newID, newFriends.getFriendlist().get(newID));
                    
                    return BusinessFacade.getInstance().addFriend(userID, (int) newID);
                }
            }
        } else {
            for (Integer ID : this.friendList.keySet()) {
                if(!newFriends.getFriendlist().containsKey(ID)) {
                    this.friendList.remove(ID);
                    return BusinessFacade.getInstance().removeFriend(userID, (int) ID);
                }
            }
        }
        return false;
    }

    @Override
    public boolean addFriend(int userID, String profileName) {
        friendList.put(userID, profileName);
        return true;
    }

    @Override
    public void removeFriend(int userID) {
        friendList.remove(userID);
    }

}
