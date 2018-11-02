package Acquaintance;

public interface IPersistence {

    public ILogin Login(ILogin Login);

    public Boolean createUser(ILogin login);

    public boolean addFriend(int userID, int newFriendID);

    public boolean removeFriend(int userID, int oldFriendID);
}
