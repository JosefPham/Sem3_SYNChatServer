package Acquaintance;

import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Group 9
 */
public interface IMessage {

    int getSenderID();

    Instant getTimestamp();
    
    String getContext();
}
