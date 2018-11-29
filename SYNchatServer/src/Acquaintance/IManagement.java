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
    public int getAction();

    public String getPw();

    public void setPw(String pw);

    public String getMail();

    public void setMail(String mail);

    public IProfile getProfile();

    public void setProfile(IProfile profile);
}
