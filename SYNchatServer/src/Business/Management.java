/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Acquaintance.IManagement;
import Acquaintance.IProfile;

/**
 *
 * @author Alexa
 */
public class Management implements IManagement {
    
    int action;
    String pw;
    String mail;
    IProfile profile;

    //Method for creating an object of Management for changeMail purposes
    public Management(int action) {
        this.action = action;
    }

    @Override
    public int getAction() {
        return action;
    }


    @Override
    public String getPw() {
        return pw;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public IProfile getProfile() {
        return profile;
    }

    @Override
    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public void setProfile(IProfile profile) {
        this.profile = profile;
    }
    
    
}
