package Acquaintance;

import java.io.Serializable;

/**
 *
 * @author Group 9
 */
public interface IManagement extends Serializable {

    /**
     * Action int = task. 0 boolean check password 1 boolean check mail 2 update
     * entire profile
     *
     * @return
     */
    int getAction();

    String getPw();

    void setPw(String pw);

    String getMail();

    void setMail(String mail);

    IProfile getProfile();

    void setProfile(IProfile profile);
}
