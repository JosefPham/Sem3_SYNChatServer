package Starter;

import Acquaintance.IBusiness;
import Acquaintance.IConnection;
import Acquaintance.IPersistence;
import Business.BusinessFacade;
import Connection.ConnectionFacade;
import Persistence.PersistenceFacade;



public class Starter {
    public static void main(String[] args) {
        IBusiness business = BusinessFacade.getInstance();
        IConnection con = ConnectionFacade.getInstance();
        IPersistence per = PersistenceFacade.getInstance();
        
        business.injectPersistence(per);
        con.injectBusiness(business);
        
        
    }

}
