package Business;

import Acquaintance.ILogin;


public class ServerSystem {
    
    private static ServerSystem instance = null;
    
    private ServerSystem(){
    }
    
    static ServerSystem getInstance() {
        if (instance == null) {
            instance = new ServerSystem();
        }
        return instance;
    }

    ILogin login(ILogin log) {
        ILogin login = new Login(log.gethMail(), log.gethPW());
        
        ILogin DBlog = BusinessFacade.getInstance().checkLoginDB(login);
        
        if (DBlog.getLoginvalue() == 2) {
            // add user to server
            // maybe check stuff? Does the user have any chats?
        }
        
        return DBlog;
    }
    
    

}
