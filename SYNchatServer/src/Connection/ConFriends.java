package Connection;

import Acquaintance.IFriends;
import java.util.HashMap;
import java.util.Map;



public class ConFriends implements IFriends{
    
     Map<Integer,String> friendlist;

    public ConFriends(Map<Integer, String> friends) {
        this.friendlist = new HashMap<>();
        for(Integer id: friends.keySet()){
            friendlist.put(id, friends.get(id));
        }
    }
     
     

    @Override
    public Map<Integer, String> getFriendlist() {
        return friendlist; 
    }

    @Override
    public boolean addFriend(int userID, String profileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeFriend(int userID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }
}
