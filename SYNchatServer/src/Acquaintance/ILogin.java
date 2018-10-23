package Acquaintance;

public interface ILogin {

    public String gethMail();

    public String gethPW();
    
        
    public int getLoginvalue();
    
    public IUser getUser();

    public int login(int loginValue);
}
