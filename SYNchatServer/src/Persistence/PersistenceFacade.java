package Persistence;

import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IPersistence;

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
    public boolean changeMail(IManagement management, int userID) {
        return sqlDatabase.changeMail(management, userID);
    }

    @Override
    public boolean changePw(IManagement management, int userID) {
        return sqlDatabase.changePw(management, userID);
    }

    @Override
    public boolean verify(IManagement management, int userID) {
        return sqlDatabase.verify(management, userID);
    }

    @Override
    public boolean alterProfile(IManagement management, int userID) {
        return sqlDatabase.alterProfile(management, userID);
    }

    @Override
    public boolean addFriend(int userID, int newFriendID) {
        return sqlDatabase.addFriend(userID, newFriendID);
    }

    @Override
    public boolean removeFriend(int userID, int oldFriendID) {
        return sqlDatabase.removeFriend(userID, oldFriendID);
    }
}
