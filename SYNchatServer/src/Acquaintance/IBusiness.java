package Acquaintance;

import java.util.Map;

/**
 *
 * @author Group 9
 */
public interface IBusiness {

    public void injectPersistence(IPersistence per);

    public ILogin checkLogin(ILogin login);

    public Boolean createUser(ILogin login);

    public boolean changeInfo(IManagement management, int userID);

    public boolean updateProfile(IManagement management, int userID);

    public boolean alterProfile(IManagement management, int userID);

    public boolean updateMailSQL(IManagement management, int userID);

    public boolean updatePwSQL(IManagement management, int userID);

    public void removeOnlineUser(int userID);

    public Map updatePublicChatUsers(int userID);

    public boolean updateFriends(IFriends friends, int userID);

    public boolean verify(IManagement management, int userID);
}
