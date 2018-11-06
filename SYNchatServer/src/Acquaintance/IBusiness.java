package Acquaintance;

public interface IBusiness {

    public void injectPersistence(IPersistence per);

    public ILogin checkLogin(ILogin login);

    public Boolean createUser(ILogin login);
    
    public int changeInfo(IManagement management);
    
    public boolean updateProfile(IProfile profile);
    
    public boolean alterProfile(IProfile profile);
    
    public int updateMailSQL(IManagement management);
    
    public int updatePwSQL(IManagement management);

}
