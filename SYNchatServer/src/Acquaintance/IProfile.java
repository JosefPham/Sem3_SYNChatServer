/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Acquaintance;

import java.io.Serializable;
import javafx.scene.image.Image;

/**
 * @author Group 9
 */
public interface IProfile extends Serializable {

    public void setFirstName(String firstName);

    public void setLastName(String lastName);

    public void setNationality(Nationality nationality);

    public void setProfileText(String profileText);

    public String getFirstName();

    public String getLastName();

    public Nationality getNationality();

    public String getProfileText();

    public Image getPicture();

    public void setPicture(Image picture);
}
