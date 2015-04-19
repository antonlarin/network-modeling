package networkmodeling.server;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStarter {

    public static void main(String[] args) {
        
        try {
            
            serverModel model = new serverModel();
            serverUIMainWindow mainWindow = new serverUIMainWindow(model);
            mainWindow.show();
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerStarter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
