package Business;

import Acquaintance.ILogin;
import Acquaintance.IManagement;
import Acquaintance.IProfile;

public class ServerSystem {

    private static ServerSystem instance = null;

    private ServerSystem() {
    }

    static ServerSystem getInstance() {
        if (instance == null) {
            instance = new ServerSystem();
        }
        return instance;
    }

    ILogin login(ILogin log) {
        ILogin login = new Login(log.gethMail(), log.gethPW());

        ILogin DBlog = BusinessFacade.getInstance().checkLoginDB(login);

        if (DBlog.getLoginvalue() == 2) {
            // add user to server
            // maybe check stuff? Does the user have any chats?
        }

        return DBlog;
    }

    protected int changeInfo(IManagement management) {
        int returnstatus = 0;
        IManagement tmpManagement = new Management(management.getAction(), management.getUserID(), management.getPw(), management.getString1());
        //Action 1 = changeMail
        //Action 2 = changePw
        if (tmpManagement.getAction() == 1) {
            returnstatus = BusinessFacade.getInstance().updateMailSQL(management);
        } else if (tmpManagement.getAction() == 2) {
            returnstatus = BusinessFacade.getInstance().updatePwSQL(management);
        }
        return returnstatus;
    }

    protected boolean updateProfile(IProfile profile) {
        return BusinessFacade.getInstance().alterProfile(profile);
    }

}
