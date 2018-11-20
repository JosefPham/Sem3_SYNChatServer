package Acquaintance;

public interface IPersistence {

    public ILogin Login(ILogin Login);

    public Boolean createUser(ILogin login);
    
    public boolean changeMail(IManagement management, int userID);
    
    public boolean changePw(IManagement management, int userID);
    
    public boolean alterProfile(IManagement management, int userID);

    public boolean addFriend(int userID, int newFriendID);

    public boolean removeFriend(int userID, int oldFriendID);
    
    public boolean verify(IManagement management, int userID);

}
