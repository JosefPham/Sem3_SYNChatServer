package Connection;

import Acquaintance.ILogin;
import Acquaintance.IUser;

/**
 *
 * @author Group 9
 */
public class ConLogin implements ILogin {

    private String hMail = null;
    private String hPW = null;
    private int loginvalue;
    private IUser user;

    /**
     * Use for when creating a login object to return back up the system. case 0
     * and 1 return user= null case 2 return user from db
     *
     * loginvalue shuld be 0 if the email dosent exist, 1 if email and pw
     * dont match, and 2 if login is sucsessfull.
     * user case(loginvalue) 0 and 1 return user= null case 2 return user
     * from db
     * @param login
     */
    public ConLogin(ILogin login) {
        this.hMail = login.gethMail();
        this.hPW = login.gethPW();
        this.loginvalue = login.getLoginvalue();
        if(login.getUser() != null)
        this.user = new ConUser(login.getUser());
    }

    @Override
    public String gethMail() {
        return hMail;
    }

    @Override
    public String gethPW() {
        return hPW;
    }

    @Override
    public int getLoginvalue() {
        return loginvalue;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public int login(ILogin finalLogin) {

        if (finalLogin.getLoginvalue() == 2) {

            //fetch and inplement user object through socket
        }
        return finalLogin.getLoginvalue();
    }

    @Override
    public void setLoginvalue(int loginvalue) {
        this.loginvalue = loginvalue;
    }

    @Override
    public void setUser(IUser user) {
        this.user = user;
    }

}
