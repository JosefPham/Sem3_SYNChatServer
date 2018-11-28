package Business;

import Acquaintance.IBusiness;
import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IPersistence;
import java.util.Map;

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
     return serversys.login(login);
    }
    
    ILogin checkLoginDB(ILogin login) {
        ILogin tmpLogin = persistence.Login(login);
        ILogin returnLogin = new Login(tmpLogin);
        return returnLogin;
    }

    @Override
    public Boolean createUser(ILogin login) {
        return persistence.createUser(login);
    }
    
    @Override
    public boolean changeInfo(IManagement management, int userID) {
        return serversys.changeInfo(management, userID);
    }
    
    @Override
    public boolean updateProfile(IManagement management, int userID) {
        return serversys.updateProfile(management, userID);
    }
    
    @Override
    public boolean alterProfile(IManagement management, int userID) {
        return persistence.alterProfile(management, userID);
    }
    
    @Override
    public boolean updateMailSQL(IManagement management, int userID) {
        return persistence.changeMail(management, userID);
    }
    
    @Override
    public boolean updatePwSQL(IManagement management, int userID) {
        return persistence.changePw(management, userID);
    }

    @Override
    public boolean updateFriends(IFriends friends, int userID) {
        return ServerSystem.getInstance().updateFriends(friends, userID);
    }

    protected boolean addFriend(int userID, int newFriendID) {
        return persistence.addFriend(userID, newFriendID);
    }

    protected boolean removeFriend(int userID, int oldFriendID) {
        return persistence.removeFriend(userID, oldFriendID);
    }

    @Override
    public void removeOnlineUser(int userID) {
        serversys.removeOnlineUser(userID);
    }
    
    @Override
    public Map updatePublicChatUsers(int userID){
        return serversys.updatePublicChatUsers(userID);
    }

    @Override
    public boolean verify(IManagement management, int userID) {
       return persistence.verify(management, userID);
    }

}
