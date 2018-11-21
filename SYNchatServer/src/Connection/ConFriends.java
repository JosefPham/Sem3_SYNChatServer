package Connection;

import Acquaintance.IFriends;
import java.util.ArrayList;
import java.util.List;

public class ConFriends implements IFriends {

    List<Integer> friendlist;

    public ConFriends(IFriends friends) {
        this.friendlist = friends.getFriendlist();
    }

    public ConFriends(List<Integer> friends) {
        this.friendlist = new ArrayList<>();
        for (Integer id : friends) {
            friendlist.add(id);
        }
    }

    @Override
    public List<Integer> getFriendlist() {
        return friendlist;
    }

}
