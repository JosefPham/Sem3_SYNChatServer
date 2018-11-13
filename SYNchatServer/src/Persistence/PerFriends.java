/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Acquaintance.IFriends;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Peter
 */
public class PerFriends implements IFriends {

    
    Map<Integer, String> friendlist = new HashMap<>();
    
    
    public PerFriends(Map<Integer, String> friendlist) {
        for (Integer key : friendlist.keySet()) {
            this.friendlist.put(key, friendlist.get(key));
        }
    }
    
    @Override
    public Map<Integer, String> getFriendlist() {
        return friendlist;
    }

    @Override
    public boolean addFriend(int userID, String profileName) {
        if (!friendlist.containsKey(userID)) {
            friendlist.put(userID, profileName);
            return true;
        } else {
            System.out.println("user is already in friendlist");
            return false;
        }
    
    }

    @Override
    public void removeFriend(int userID) {
        friendlist.remove(userID);
    }
    
}
