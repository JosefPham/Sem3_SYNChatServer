package Persistence;

import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IPersistence;
import Acquaintance.IProfile;
import Acquaintance.IUser;

public class PersistenceFacade implements IPersistence {

    private static PersistenceFacade instance;
    private DatabaseHandler sqlDatabase = new DatabaseHandler();

    /**
     * Private Constructor, requred for singleton
     */
    private PersistenceFacade() {

    }

    /**
     * Getter method for singleton, requered for layered architecture
     *
     * @return
     */
    public static PersistenceFacade getInstance() {
        if (instance == null) {
            instance = new PersistenceFacade();
        }
        return instance;
    }

    @Override
    public ILogin Login(ILogin Login) {
        return sqlDatabase.Login(Login);
    }

    @Override
    public Boolean createUser(ILogin login) {
        return sqlDatabase.createUser(login);
    }
    
    @Override
    public int changeMail(IManagement management) {
        return sqlDatabase.changeMail(management);
    }
    
    @Override
    public int changePw(IManagement management) {
        return sqlDatabase.changePw(management);
    }
    
    @Override
    public boolean alterProfile(IUser user) {
        return sqlDatabase.alterProfile(user);
    }
}
