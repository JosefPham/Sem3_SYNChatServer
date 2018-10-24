package Persistence;

import Acquaintance.ILogin;
import Acquaintance.IPersistence;
import Acquaintance.IUser;
import java.util.ArrayList;
import java.util.List;


public class TestMain {
    
    public static void main(String[] args) {
        IPersistence facade = PersistenceFacade.getInstance();
        
        List<Integer> chats = new ArrayList<>();
        IUser user = new PerUser(-1, "Rasmus", true, 0, chats);
        ILogin login = new PerLogin("test@test.dk", "12345678", -1, user);
        
        facade.createUser(login, user);
        
        
        
    }

}
