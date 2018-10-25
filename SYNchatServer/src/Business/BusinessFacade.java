package Business;

import Acquaintance.IBusiness;
import Acquaintance.ILogin;
import Acquaintance.IPersistence;
import Acquaintance.IUser;


public class BusinessFacade implements IBusiness{
    
    private IPersistence persistence;
    private ServerSystem serversys = ServerSystem.getInstance();
    
    private static BusinessFacade instance;
    
    /**
     * Private Constructor, requred for singleton
     */
    private BusinessFacade(){
        
    }
    
    /**
     * getter method for singleton, requered for layered architecture
     * @return the facade itself.
     */
    public static BusinessFacade getInstance() {
        
            if(instance == null){
            instance = new BusinessFacade();
        }
        return instance;
    }

    /**
     * Acquaints this with the persistance facade through the "gluecode" in the starter pack
     * @param per 
     */
    @Override
    public void injectPersistence(IPersistence per) {
       this.persistence = per;
    }
    
    @Override
    public ILogin checkLogin(ILogin login) {
     //  Login log = new Login(login.gethMail(), login.gethPW());
     //  log.setLoginvalue(2);
     
     return serversys.login(login);
     
//       ILogin datalogin = persistence.Login(login);
//       System.out.println("chats: " + datalogin.getUser().getChats());
//       System.out.println(datalogin.getLoginvalue());
//        
//       IUser retUser = new User(datalogin.getUser().getUserID(), datalogin.getUser().getTmpName(), datalogin.getUser().isBanned(), datalogin.getUser().getReports(), datalogin.getUser().getChats());
//       ILogin retLogin = new Login(datalogin.getLoginvalue(), retUser);
//       return retLogin;
       //return log;
    }
    
    ILogin checkLoginDB(ILogin login) {
        return persistence.Login(login);
    }

    @Override
    public Boolean createUser(ILogin login) {
        return persistence.createUser(login);
    }
    
    

}
