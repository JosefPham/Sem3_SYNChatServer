package Persistence;

import Acquaintance.ILogin;
import Acquaintance.IPersistence;


public class PersistenceFacade implements IPersistence{
    
    private static PersistenceFacade instance;
    private DatabaseHandler sqlDatabase = new DatabaseHandler();
    
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

    @Override
    public ILogin Login(ILogin Login){
        return sqlDatabase.Login(Login);
    }
    
    @Override
    public Boolean createUser(ILogin login){
        return sqlDatabase.createUser(login);
    }
}
