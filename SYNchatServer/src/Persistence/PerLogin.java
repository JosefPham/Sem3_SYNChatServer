package Persistence;

import Acquaintance.ILogin;
import Acquaintance.IUser;
/**
 * class used for returning a login object containing loginvalue and User
 */
public class PerLogin implements ILogin {

     private String hMail = null;
    private  String hPW =null;
    private int loginvalue;
    private IUser user;
    
    /**
     * Use for when creating a login object to return back up the system. case 0 and 1 return user= null case 2 return user from db
     * @param loginvalue shuld be 0 if the email dosent exist, 1 if email and pw dont match, and 2 if login is sucsessfull.
     * @param user case(loginvalue) 0 and 1 return user= null case 2 return user from db
     */
    PerLogin(int loginvalue, IUser user) {
        this.loginvalue = loginvalue;
        this.user = user;
    }
    
    PerLogin(String mail, String pw, int value, IUser user){
        this.hMail = mail;
        this.hPW = pw;
        this.loginvalue = value;
        this.user = user;
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
    public int login(int loginValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
