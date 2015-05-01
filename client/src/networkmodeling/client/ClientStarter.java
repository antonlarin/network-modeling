package networkmodeling.client;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkmodeling.core.*;

public class ClientStarter {

    public static void main(String[] args) {
        
        Client c = new Client();
        c.connectToServer();
        try {
            sleep(800); 
      //      c.SendAddDevicesRequest(new Hub(new MacAddress(), 2));
            sleep(900);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientStarter.class.getName()).log(Level.SEVERE, null, ex);
        }
        c.disconnect();
    }
}
