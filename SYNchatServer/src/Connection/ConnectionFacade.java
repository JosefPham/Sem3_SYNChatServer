package Connection;

import Acquaintance.IBusiness;
import Acquaintance.IConnection;
import Acquaintance.ILogin;

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
    public static IConnection getInstance() {
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
        return business.checkLogin(login);
    }

    public Boolean createUser(ILogin login) {
        return business.createUser(login);
    }
}
