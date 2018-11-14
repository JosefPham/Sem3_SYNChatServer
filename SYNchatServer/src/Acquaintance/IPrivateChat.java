package Acquaintance;

import java.util.List;

public interface IPrivateChat {
public List<Integer> getUserIDs();
public int getChatID();
public String getName();
public IChatHistory getCh();
}
