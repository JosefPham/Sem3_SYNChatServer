/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import Acquaintance.IManagement;

/**
 *
 * @author Alexa
 */
public class Management implements IManagement {
    
    int action;
    int userID;
    String newPw;
    String pw;
    String string1;

    //Method for creating an object of Management for changeMail purposes
    public Management(int action, int userID, String pw, String string1) {
        this.action = action;
        this.userID = userID;
        this.pw = pw;
        this.string1 = string1;
    }

    @Override
    public int getAction() {
        return action;
    }

    @Override
    public int getUserID() {
        return userID;
    }

    @Override
    public String getPw() {
        return pw;
    }

    @Override
    public String getString1() {
        return string1;
    }
    
    
}
