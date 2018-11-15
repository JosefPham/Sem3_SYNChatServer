package Acquaintance;

import java.util.Map;

public interface IBusiness {

    public void injectPersistence(IPersistence per);

    public ILogin checkLogin(ILogin login);

    public Boolean createUser(ILogin login);
    
    public int changeInfo(IManagement management, int userID);
    
    public boolean updateProfile(IUser user);
    
    public boolean alterProfile(IUser user);
    
    public int updateMailSQL(IManagement management);
    
    public int updatePwSQL(IManagement management);
    
    public void removeOnlineUser(int userID);
    
    public Map updatePublicChatUsers(int userID);

    public IFriends updateFriends(IFriends friends, int userID);
}
