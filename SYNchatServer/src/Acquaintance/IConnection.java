package Acquaintance;

public interface IConnection {

    public void injectBusiness(IBusiness business);
    
    public ILogin checkLogin(ILogin login);
    
    public Boolean createUser(ILogin login);

}
