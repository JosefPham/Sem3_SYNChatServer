package Acquaintance;

public interface IBusiness {

    public void injectPersistence(IPersistence per);
    public ILogin checkLogin(ILogin login);

}
