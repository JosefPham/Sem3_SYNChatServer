package Starter;

<<<<<<< HEAD
import Acquaintance.IBusiness;
import Acquaintance.IConnection;
import Acquaintance.IPersistence;
import Business.BusinessFacade;
import Connection.ConnectionFacade;
import Persistence.PersistenceFacade;


=======
import Connection.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 * Test server
 */
public class SYNchatServer {
>>>>>>> Connection

public class SYNchatServer {
    public static void main(String[] args) {
        
<<<<<<< HEAD
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
        
        
=======
         try {
            InetAddress ip = (InetAddress) InetAddress.getByName("10.126.33.99");
            int port = 8080;
            Server server = new Server(ip, port);
            
           // Thread t = new Thread(server);
        //    t.start();
        } catch (IOException ex) {
            Logger.getLogger(SYNchatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
>>>>>>> Connection
    }

}