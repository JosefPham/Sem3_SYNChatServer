package Business;

import Acquaintance.IBusiness;
import Acquaintance.IPersistence;


public class BusinessFacade implements IBusiness{
    
    private IPersistence persistence;
    
    private static BusinessFacade instance;
    
    private BusinessFacade(){
        
    }

    public static IBusiness getInstance() {
        
            if(instance == null){
            instance = new BusinessFacade();
        }
        return instance;
    }

    @Override
    public void injectPersistence(IPersistence per) {
       this.persistence = per;
    }

}
