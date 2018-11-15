package Acquaintance;

public interface IPersistence {

    public ILogin Login(ILogin Login);

    public Boolean createUser(ILogin login);
    
    public int changeMail(IManagement management, int userID);
    
    public int changePw(IManagement management, int userID);
    
    public boolean alterProfile(IUser user);

    public boolean addFriend(int userID, int newFriendID);

    public boolean removeFriend(int userID, int oldFriendID);

}
