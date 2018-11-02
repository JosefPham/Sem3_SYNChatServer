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
    
    boolean updateFriends(IFriends friends) {
        
    }

}
