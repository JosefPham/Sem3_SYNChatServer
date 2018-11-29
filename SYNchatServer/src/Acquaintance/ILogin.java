package Acquaintance;

import java.io.Serializable;

/**
 *
 * @author Group 9
 */
public interface ILogin extends Serializable {

    public String gethMail();

    public String gethPW();

    public int getLoginvalue();

    public IUser getUser();

    public void setLoginvalue(int loginvalue);

    public void setUser(IUser user);

    public int login(ILogin finalLogin);
}
