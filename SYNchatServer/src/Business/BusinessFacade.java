package Business;

import Acquaintance.IBusiness;
import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IPersistence;
import Acquaintance.IProfile;
import Acquaintance.IUser;

public class BusinessFacade implements IBusiness {

    private IPersistence persistence;

    private ServerSystem serversys = ServerSystem.getInstance();
    
    private static BusinessFacade instance;

    /**
     * Private Constructor, requred for singleton
     */
    private BusinessFacade() {

    }

    /**
     * getter method for singleton, requered for layered architecture
     *
     * @return the facade itself.
     */
    public static BusinessFacade getInstance() {
        
            if(instance == null){
            instance = new BusinessFacade();
        }
        return instance;
    }

    /**
     * Acquaints this with the persistance facade through the "gluecode" in the
     * starter pack
     *
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
    
    @Override
    public int changeInfo(IManagement management) {
        return serversys.changeInfo(management);
    }
    
    @Override
    public boolean updateProfile(IUser user) {
        return serversys.updateProfile(user);
    }
    
    @Override
    public boolean alterProfile(IUser user) {
        return persistence.alterProfile(user);
    }
    
    @Override
    public int updateMailSQL(IManagement management) {
        return persistence.changeMail(management);
    }
    
    @Override
    public int updatePwSQL(IManagement management) {
        return persistence.changePw(management);
    }

    @Override
    public IFriends updateFriends(IFriends friends, int userID) {
        return ServerSystem.getInstance().updateFriends(friends, userID);
    }

    boolean addFriend(int userID, int newFriendID) {
        return persistence.addFriend(userID, newFriendID);
    }

    boolean removeFriend(int userID, int oldFriendID) {
        return persistence.removeFriend(userID, oldFriendID);
    }

}
