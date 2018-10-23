package Business;

import Acquaintance.IBusiness;
import Acquaintance.IPersistence;


public class BusinessFacade implements IBusiness{
    
    private IPersistence persistence;
    
    private static BusinessFacade instance;
    
    /**
     * Private Constructor, requred for singleton
     */
    private BusinessFacade(){
        
    }
    
    /**
     * getter method for singleton, requered for layered architecture
     * @return the facade itself.
     */
    public static IBusiness getInstance() {
        
            if(instance == null){
            instance = new BusinessFacade();
        }
        return instance;
    }

    /**
     * Acquaints this with the persistance facade through the "gluecode" in the starter pack
     * @param per 
     */
    @Override
    public void injectPersistence(IPersistence per) {
       this.persistence = per;
    }

}