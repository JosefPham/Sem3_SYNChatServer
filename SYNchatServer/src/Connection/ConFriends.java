package Connection;

import Acquaintance.IFriends;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class ConFriends implements IFriends{
    
     List<Integer> friendlist;

    public ConFriends(List<Integer> friends) {
        this.friendlist = new ArrayList<>();
        for(Integer id: friends){
            friendlist.add(id);
        }
    }
     
     

    @Override
    public List<Integer> getFriendlist() {
        return friendlist; 
    }

}
