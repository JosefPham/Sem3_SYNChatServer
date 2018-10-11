package Starter;

import Acquaintance.IBusiness;
import Acquaintance.IConnection;
import Acquaintance.IPersistence;
import Business.BusinessFacade;
import Connection.ConnectionFacade;
import Persistence.PersistenceFacade;



public class SYNchatServer {
    public static void main(String[] args) {
        
        /**
         * The aquiering af the facade instanceses, and there trough thire creation
         */
        IBusiness business = BusinessFacade.getInstance();
        IConnection con = ConnectionFacade.getInstance();
        IPersistence per = PersistenceFacade.getInstance();
        
        /**
         * Calls the injects on the business and connection facads which acquaints them with the facade under then trhough there interface
         */
        business.injectPersistence(per);
        con.injectBusiness(business);
        
        
    }

}