/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Acquaintance.IProfile;
import Acquaintance.Nationality;
import javafx.scene.image.Image;

/**
 *
 * @author Sigurd E. Espersen
 */
public class ConProfile implements IProfile {
    private String firstName;
    private String lastName;
    private Nationality nationality;
    private Image picture; //Watch out for the datatype!
    private String profileText;
    
    public ConProfile(String firstName, String lastName, Nationality nationality, String profileText) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.profileText = profileText;
    }
    
    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    @Override
    public void setProfileText(String profileText) {
        this.profileText = profileText;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }
    
    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public Nationality getNationality() {
        return nationality;
    }

    @Override
    public String getProfileText() {
        return profileText;
    }
}
