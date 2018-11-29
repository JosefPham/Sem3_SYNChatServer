package Acquaintance;

import java.util.Map;

/**
 *
 * @author Group 9
 */
public interface IConnection {

    void injectBusiness(IBusiness business);

    ILogin checkLogin(ILogin login);

    Boolean createUser(ILogin login);

    boolean changeInfo(IManagement management, int userID);

    boolean updateProfile(IManagement management, int userID);

    Map updatePublicChatUsers(int userID);

}
