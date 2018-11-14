/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Acquaintance.IFriends;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Peter
 */
public class PerFriends implements IFriends {

    
    List<Integer> friendlist = new ArrayList<>();
    
    
    public PerFriends(List<Integer> friendlist) {
        for (Integer key : friendlist) {
            this.friendlist.add(key);
        }
    }
    
    @Override
    public List<Integer> getFriendlist() {
        return friendlist;
    }

    public boolean addFriend(int userID, String profileName) {
        if (!friendlist.contains(userID)) {
            friendlist.add(userID);
            return true;
        } else {
            System.out.println("user is already in friendlist");
            return false;
        }
    
    }

    public void removeFriend(int userID) {
        friendlist.remove(userID);
    }
    
}
