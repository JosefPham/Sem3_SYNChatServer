package Acquaintance;

/**
 *
 * @author Group 9
 */
public interface IPersistence {

    ILogin Login(ILogin Login);

    Boolean createUser(ILogin login);

    boolean changeMail(IManagement management, int userID);

    boolean changePw(IManagement management, int userID);

    boolean alterProfile(IManagement management, int userID);

    boolean addFriend(int userID, int newFriendID);

    boolean removeFriend(int userID, int oldFriendID);

    boolean verify(IManagement management, int userID);

}
