package Acquaintance;

import java.io.Serializable;
import java.util.List;

public interface IUser extends Serializable {

    public int getUserID();

    public String getTmpName();

    public boolean isBanned();

    public int getReports();

    public List<Integer> getChats();
}
