/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Acquaintance;

/**
 *
 * @author Alexa
 */
public interface IManagement {
    
    public int getAction();

    public String getPw();

    public String getMail();
    
    public IProfile getProfile();
    
    public void setPw(String pw);
    
    public void setMail(String mail);
    
    public void setProfile(IProfile profile);
    
    
    
}
