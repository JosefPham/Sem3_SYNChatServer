package Acquaintance;

public interface IPersistence {

    public ILogin Login(ILogin Login);

    public Boolean createUser(ILogin login);
}
