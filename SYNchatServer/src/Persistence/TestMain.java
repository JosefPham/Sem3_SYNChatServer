/*
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
        //IUser user = new PerUser(-1, "Rasmus", true, 0, chats);
        ILogin login = new PerLogin("test@test11.dk", "12345678", -1, null);
        
       // System.out.println("mail: " + login.gethMail() + " pw: " + login.gethPW() + " name: " + login.getUser().getTmpName());
        //facade.createUser(login);
        ILogin returnLogin = facade.Login(login);
        System.out.println("loginvalue: " + returnLogin.getLoginvalue());
       // System.out.println("user id " + returnLogin.getUser().getUserID());
       // System.out.println("name" + returnLogin.getUser().getTmpName());
       for(Integer i : returnLogin.getUser().getChats()){
           System.out.println("chats: " + i);
       }

        
        
        
    }

}
*/