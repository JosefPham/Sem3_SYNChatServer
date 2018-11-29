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

    public String getPicture();

    public void setPicture(String picture);
}
