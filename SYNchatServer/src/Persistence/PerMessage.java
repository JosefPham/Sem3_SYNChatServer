package Persistence;

import Acquaintance.IMessage;
import java.sql.Timestamp;
import java.time.Instant;

abstract class PerMessage implements IMessage {
    private int senderID;
    private Instant timestamp;
    
    public PerMessage(int senderID, Timestamp time) {
        this.senderID = senderID;
        this.timestamp = time.toInstant();
    }
    
    @Override
    public void updateTimestamp() {
        this.timestamp = Instant.now();
    }

    @Override
    public int getSenderID() {
        return senderID;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
    
    @Override
    public abstract String getContext();
}