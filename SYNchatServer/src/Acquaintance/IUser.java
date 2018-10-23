package Acquaintance;

import java.util.List;


public interface IUser {
    public int getUserID();
    public String getTmpName();
    public boolean isBanned();
    public int getReports();
    public List<Integer> getChats();
}
