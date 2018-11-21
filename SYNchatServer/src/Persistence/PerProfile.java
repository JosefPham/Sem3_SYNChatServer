/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

import Acquaintance.IProfile;
import Acquaintance.Nationality;

/**
 *
 * @author Alexa
 */
public class PerProfile implements IProfile {

    String firstName;
    String lastName;
    Nationality nationality;
    String picture; //Watch out for the datatype!
    String profileText;

    public PerProfile(String firstName, String lastName, Nationality nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
    }

    public PerProfile(String firstName, String lastName, Nationality nationality, String profileText) {
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

    public void setNationality(Nationality nationality) {
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

    public Nationality getNationality() {
        return nationality;
    }

    public String getProfileText() {
        return profileText;
    }

    @Override
    public String getPicture() {
        return picture;
    }

    @Override
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
