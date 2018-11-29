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

    void setTimestamp(Instant timestamp);

    String getContext();
}
