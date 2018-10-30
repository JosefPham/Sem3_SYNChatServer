package Connection;

import Acquaintance.IBusiness;
import Acquaintance.IConnection;
import Acquaintance.ILogin;
import Acquaintance.IUser;

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
        IUser retUser = new ConUser(l.getUser().getUserID(), l.getUser().getTmpName(), l.getUser().isBanned(), l.getUser().getReports(), l.getUser().getChats());
        ILogin log = new ConLogin(l.gethMail(), l.gethPW(), l.getLoginvalue(), retUser);
        return log;
    }

    @Override
    public Boolean createUser(ILogin login) {
        return business.createUser(login);
    }
}
