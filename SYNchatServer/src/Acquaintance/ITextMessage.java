package Acquaintance;

import java.time.Instant;

/**
 *
 * @author Group 9
 */
public interface ITextMessage {

    int getSenderID();

    Instant getTimestamp();

    String getContext();
}
