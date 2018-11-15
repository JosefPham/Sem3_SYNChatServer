/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Acquaintance.IManagement;

/**
 *
 * @author Alexa
 */
public class ConManagement implements IManagement {
    
    int action;
    String newPw;
    String pw;
    String string1;

    //Method for creating an object of Management for changeMail purposes
    public ConManagement(int action, String pw, String string1) {
        this.action = action;
        this.pw = pw;
        this.string1 = string1;
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
    public String getString1() {
        return string1;
    }
    
}
