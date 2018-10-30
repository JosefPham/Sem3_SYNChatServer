/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Acquaintance.ILogin;
import Acquaintance.IUser;

/**
 *
 * @author Peter
 */
public class ConLogin implements ILogin{

     private String hMail = null;
    private String hPW = null;
    private int loginvalue;
    private IUser user;

    /**
     * Use for when creating a login object to return back up the system. case 0
     * and 1 return user= null case 2 return user from db
     *
     * @param loginvalue shuld be 0 if the email dosent exist, 1 if email and pw
     * dont match, and 2 if login is sucsessfull.
     * @param user case(loginvalue) 0 and 1 return user= null case 2 return user
     * from db
     */
    ConLogin(int loginvalue, IUser user) {
        this.loginvalue = loginvalue;
        this.user = user;
    }

    ConLogin(String mail, String pw, int value, IUser user) {
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

        if (loginValue == 2) {

            //fetch and inplement user object through socket
        }
        return loginValue;
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
