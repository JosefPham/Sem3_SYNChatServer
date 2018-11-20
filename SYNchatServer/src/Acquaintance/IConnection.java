package Acquaintance;

import java.util.Map;

public interface IConnection {

    public void injectBusiness(IBusiness business);

    public ILogin checkLogin(ILogin login);

    public Boolean createUser(ILogin login);
    
    public boolean changeInfo(IManagement management, int userID);
    
    public boolean updateProfile(IManagement management, int userID);
    
    public Map updatePublicChatUsers(int userID);

}
