package Acquaintance;

import java.util.Map;

public interface IBusiness {

    public void injectPersistence(IPersistence per);

    public ILogin checkLogin(ILogin login);

    public Boolean createUser(ILogin login);
    
    public boolean changeInfo(IManagement management, int userID);
    
    public boolean updateProfile(IUser user);
    
    public boolean alterProfile(IUser user);
    
    public boolean updateMailSQL(IManagement management, int userID);
    
    public boolean  updatePwSQL(IManagement management, int userID);
    
    public void removeOnlineUser(int userID);
    
    public Map updatePublicChatUsers(int userID);

    public IFriends updateFriends(IFriends friends, int userID);
    
    public boolean verify(IManagement management, int userID);
}
