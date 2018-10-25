/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Acquaintance.ILogin;
import Acquaintance.IUser;
import java.io.Serializable;

/**
 *
 * @author Group 9
 */
public class Login implements ILogin, Serializable {
    
    
    private String hMail;
    private String hPW;
    private int loginvalue;
    private IUser user;
    
    public Login(String mail, String pw) {
        hMail = mail;
        hPW = pw;
    }
    
      public Login(int loginvalue, IUser user) {
        this.loginvalue = loginvalue;
        this.user = user;
        this.hMail = null;
        this.hPW = null;
    }
    

    @Override
    public String gethMail() {
        return hMail;
    }

    @Override
    public String gethPW() {
        return hPW;
    }

    public int getLoginvalue() {
        return loginvalue;
    }

    public IUser getUser() {
        return user;
    }

    public void setLoginvalue(int loginvalue) {
        this.loginvalue = loginvalue;
    }

    public void setUser(IUser user) {
        this.user = user;
    }
    
    
    
    
    @Override
    public int login(int loginValue) {
               
        if (loginValue == 2) {
            
            //fetch and inplement user object through socket
        }
        return loginValue;
    }
}
