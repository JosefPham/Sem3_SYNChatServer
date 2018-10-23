package Acquaintance;

import java.io.Serializable;

public interface ILogin extends Serializable {

    public String gethMail();

    public String gethPW();
    
        
    public int getLoginvalue();
    
    public IUser getUser();

    public int login(int loginValue);
}
