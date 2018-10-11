package Persistence;

import Acquaintance.IPersistence;


public class PersistenceFacade implements IPersistence{
    
    private static PersistenceFacade instance;
    
    /**
     * Private Constructor, requred for singleton
     */
    private PersistenceFacade(){
        
    }
    
    /**
     * Getter method for singleton, requered for layered architecture
     * @return 
     */
    public static IPersistence getInstance() {
        if(instance == null){
            instance = new PersistenceFacade();
        }
        return instance;
    }

}
