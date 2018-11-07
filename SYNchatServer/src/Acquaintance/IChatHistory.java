/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Acquaintance;

import java.util.List;

/**
 *
 * @author Patrick
 */
public interface IChatHistory {
    
    int loadMoreMessages();
    public int getMsgLoadedCount();
    public List<IMessage> getMsgList();
}
