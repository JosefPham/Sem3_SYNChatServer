package Connection;

import Acquaintance.IBusiness;
import Acquaintance.IConnection;
import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IProfile;
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
        if(l.getLoginvalue() == 2){
        IProfile profile = new ConProfile(l.getUser().getProfile().getFirstName(), l.getUser().getProfile().getLastName(), l.getUser().getProfile().getNationality(), l.getUser().getProfile().getProfileText());
        ConFriends friends = new ConFriends(l.getUser().getFriends().getFriendlist());
        ConUser retUser = new ConUser(l.getUser().getUserID(), l.getUser().isBanned(), l.getUser().getReports(), l.getUser().getChats(), l.getUser().getFriends(), profile);
        
        retUser.setFriends(friends);
        ILogin log = new ConLogin(l.gethMail(), l.gethPW(), l.getLoginvalue(), retUser);  
         return log;
        }
        else{
         ILogin log = new ConLogin(l.getLoginvalue(), null);
          return log;
        }
        
       
    }

    @Override
    public Boolean createUser(ILogin login) {
        return business.createUser(login);
    }
    
    @Override
    public int changeInfo(IManagement management) {
        return business.changeInfo(management);
    }
    
    @Override
    public boolean updateProfile(IUser user) {
        return business.updateProfile(user);
    }
}
