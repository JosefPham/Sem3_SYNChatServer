/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Acquaintance.Nationality;
import Acquaintance.Nationality;
import javafx.scene.image.Image;

/**
 *
 * @author Alexa
 */
public class PerProfile {
    
    String firstName;
    String lastName;
    String nationality;
    Image picture; //Watch out for the datatype!
    String profileText;

    public PerProfile(String firstName, String lastName, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }
    
    public PerProfile(String firstName, String lastName, String nationality, String profileText) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.profileText = profileText;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public String getProfileText() {
        return profileText;
    }
}