package Acquaintance;

public interface IBusiness {

    public void injectPersistence(IPersistence per);

    public ILogin checkLogin(ILogin login);

    public Boolean createUser(ILogin login);

    public Boolean updateFriends(IFriends friends, int userID);

}
