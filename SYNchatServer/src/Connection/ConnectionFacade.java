package Connection;

import Acquaintance.IBusiness;
import Acquaintance.IConnection;
import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import java.util.Map;

public class ConnectionFacade implements IConnection {

    private IBusiness business;

    private static ConnectionFacade instance;

    /**
     * Private Constructor, requred for singleton
     */
    private ConnectionFacade() {

    }

    /**
     * getter method for singleton, requered for layered architecture
     *
     * @return the facade itself.
     */
    public static ConnectionFacade getInstance() {
        if (instance == null) {
            instance = new ConnectionFacade();
        }
        return instance;
    }

    /**
     * Acquaints this with the buisiness facade through the "gluecode" in the
     * starter pack
     *
     * @param business
     */
    @Override
    public void injectBusiness(IBusiness business) {
        this.business = business;
    }

    @Override
    public ILogin checkLogin(ILogin login) {
        ILogin l = business.checkLogin(login);
        if (l.getLoginvalue() == 2) {
            ILogin log = new ConLogin(l);
            return log;
        } else {
            ILogin log = new ConLogin(l);
            return log;
        }
    }

    @Override
    public Boolean createUser(ILogin login) {
        return business.createUser(login);
    }

    @Override
    public boolean changeInfo(IManagement management, int userID) {
        return business.changeInfo(management, userID);
    }

    @Override
    public boolean updateProfile(IManagement management, int userID) {
        return business.updateProfile(management, userID);
    }

    public boolean updateFriends(IFriends friends, int userID) {
        return business.updateFriends(friends, userID);
    }

    public void removeOnlineUser(int userID) {
        business.removeOnlineUser(userID);
    }

    @Override
    public Map updatePublicChatUsers(int userID) {
        return business.updatePublicChatUsers(userID);
    }
}
