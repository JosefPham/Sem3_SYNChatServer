package Persistence;

import Acquaintance.IPersistence;


public class PersistenceFacade implements IPersistence{
    
    private static PersistenceFacade instance;

    public static IPersistence getInstance() {
        if(instance == null){
            instance = new PersistenceFacade();
        }
        return instance;
    }

}
