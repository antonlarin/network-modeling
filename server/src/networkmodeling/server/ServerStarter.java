package networkmodeling.server;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStarter {

    public static void main(String[] args) {

            serverUIMainWindow mainWindow = new serverUIMainWindow();
            mainWindow.setVisible(true);
    }

}