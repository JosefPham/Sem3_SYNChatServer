package Connection;

import Acquaintance.IBusiness;
import Acquaintance.IConnection;


public class ConnectionFacade implements IConnection{
    
    private IBusiness business;

    
    private static ConnectionFacade instance;
    
    public static IConnection getInstance() {
                if(instance == null){
            instance = new ConnectionFacade();
        }
        return instance;
    }

    @Override
    public void injectBusiness(IBusiness business) {
        this.business = business;
    }
    

}
