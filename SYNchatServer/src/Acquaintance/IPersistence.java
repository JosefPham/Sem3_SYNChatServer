package Acquaintance;

public interface IPersistence {

    public ILogin Login(ILogin Login);

    public Boolean createUser(ILogin login);
    
    public int changeMail(IManagement management);
    
    public int changePw(IManagement management);
    
    public boolean alterProfile(IProfile profile);
    
}
