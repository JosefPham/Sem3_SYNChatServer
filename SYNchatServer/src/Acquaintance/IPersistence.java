package Acquaintance;

public interface IPersistence {

    public ILogin Login(ILogin Login);

    public Boolean createUser(ILogin login);
    
    public int changeMail(IManagement management);
    
    public int changePw(IManagement management);
    
    public boolean alterProfile(IUser user);


    public boolean addFriend(int userID, int newFriendID);

    public boolean removeFriend(int userID, int oldFriendID);

    public IPrivateChat addToPrivateChat(IPrivateChat prichat);

    public IPrivateChat createNewPrivateChat(IPrivateChat prichat);
    

}
