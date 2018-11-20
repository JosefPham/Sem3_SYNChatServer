package Connection;

import Acquaintance.IManagement;
import Acquaintance.IProfile;

public class ConManagement implements IManagement {

    private final int action;
    private String pw, mail;
    private IProfile profile;

    public ConManagement(int action) {
        this.action = action;
    }

    @Override
    public int getAction() {
        return action;
    }

    @Override
    public String getPw() {
        return pw;
    }

    @Override
    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public String getMail() {
        return mail;
    }

    @Override
    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public IProfile getProfile() {
        return profile;
    }

    @Override
    public void setProfile(IProfile profile) {
        this.profile = profile;
    }
}