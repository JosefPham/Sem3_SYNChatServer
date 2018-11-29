package Acquaintance;

import java.util.Map;

/**
 *
 * @author Group 9
 */
public interface IBusiness {

    void injectPersistence(IPersistence per);

    ILogin checkLogin(ILogin login);

    Boolean createUser(ILogin login);

    boolean changeInfo(IManagement management, int userID);

    boolean updateProfile(IManagement management, int userID);

    boolean alterProfile(IManagement management, int userID);

    boolean updateMailSQL(IManagement management, int userID);

    boolean updatePwSQL(IManagement management, int userID);

    void removeOnlineUser(int userID);

    Map updatePublicChatUsers(int userID);

    boolean updateFriends(IFriends friends, int userID);

    boolean verify(IManagement management, int userID);
}
