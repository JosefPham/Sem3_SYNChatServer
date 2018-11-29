package Acquaintance;

import java.io.Serializable;

/**
 *
 * @author Group 9
 */
public interface ILogin extends Serializable {

    String gethMail();

    String gethPW();

    int getLoginvalue();

    IUser getUser();

    void setLoginvalue(int loginvalue);

    void setUser(IUser user);

    int login(ILogin finalLogin);
}
