package Acquaintance;

import java.io.Serializable;

/**
 * @author Group 9
 */
public interface IProfile extends Serializable {

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setNationality(Nationality nationality);

    void setProfileText(String profileText);

    String getFirstName();

    String getLastName();

    Nationality getNationality();

    String getProfileText();

    String getPicture();

    void setPicture(String picture);
}
