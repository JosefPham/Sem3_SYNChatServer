package Connection;

import Acquaintance.IProfile;
import Acquaintance.Nationality;

public class ConProfile implements IProfile {

    private String firstName;
    private String lastName;
    private Nationality nationality;
    private String picture; //Watch out for the datatype!
    private String profileText;

    public ConProfile(IProfile profile) {
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.nationality = profile.getNationality();
        this.profileText = profile.getProfileText();
        this.picture = profile.getPicture();
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
