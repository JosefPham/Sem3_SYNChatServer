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
 * @author Group 9
 */
public class PerProfile implements IProfile {

    private String firstName;
    private String lastName;
    private Nationality nationality;
    private String picture; //Watch out for the datatype!
    private String profileText;

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

    @Override
    public String getPicture() {
        return picture;
    }

    @Override
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
