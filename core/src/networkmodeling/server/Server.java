package networkmodeling.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Server extends Thread{
    public Server()
    {
        serverSocket = null;
        serverIP = null;
        serverPort = 7771;
        serverClients = new HashMap<>();
        try
        {
            serverIP = InetAddress.getLocalHost();
        }
        catch(UnknownHostException excep)
        {
            //handle
        }
    }
    
    @Override
    public void run() {
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(serverPort, 0, serverIP);
            while(true) {
                clientSocket = serverSocket.accept();
                ClientThread client = new ClientThread(this, clientSocket);
                serverClients.put(client.getUUID(), client);
                client.start();
            }
        }
        catch(IOException excep)    {
            
        }
    }
    
    public void stopServer()
    {
        try {
            serverSocket.close();          
           
            Iterator<ClientThread> i = serverClients.values().iterator();
            while (i.hasNext())
                i.next().close();
            this.stop();
        } catch (IOException ex) {
            
        }
        
    }
    
    private HashMap<UUID, ClientThread> serverClients;
    private ServerSocket serverSocket;
    private final int serverPort;
    private final InetAddress serverIP;
}
