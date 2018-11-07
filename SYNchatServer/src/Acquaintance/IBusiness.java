package Acquaintance;

public interface IBusiness {

    public void injectPersistence(IPersistence per);

    public ILogin checkLogin(ILogin login);

    public Boolean createUser(ILogin login);
    
    public int changeInfo(IManagement management);
    
    public boolean updateProfile(IUser user);
    
    public boolean alterProfile(IUser user);
    
    public int updateMailSQL(IManagement management);
    
    public int updatePwSQL(IManagement management);

    public IPrivateChat handlerPrivateChat(IPrivateChat conPrivateChat);

}
