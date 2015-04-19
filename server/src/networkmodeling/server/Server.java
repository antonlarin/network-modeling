package networkmodeling.server;

import networkmodeling.core.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Server extends Thread{
    public Server()
    {
        serverSocket = null;
        serverIP = null;
        serverPort = 7772;
        serverClients = new HashMap<>();
        eventsLog = new String();
    }
    
    @Override
    public void run() {
        Socket clientSocket;
        try {
            serverIP = InetAddress.getLocalHost();
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
    
    public void startServer()
    {
        eventsLog += "Сервер запущен.\n";
        this.start();
    }
    public void stopServer()
    {
        eventsLog += "Сервер остановлен.\n";
        
        try {
            serverSocket.close();          
           
            Iterator<ClientThread> i = serverClients.values().iterator();
            while (i.hasNext())
                i.next().close();
            
            serverClients.clear();
            this.stop();
        } catch (IOException ex) {
            
        }
    }
    public void dropClient(UUID clientID)
    {
        ClientThread clientForDrop = serverClients.get(clientID);
        if(clientForDrop != null)
        {
            clientForDrop.close();
            serverClients.remove(clientID);
        }
    }
    
    public String getServerLog()
    {
        return eventsLog;
    }
    
    private String eventsLog;
    private HashMap<UUID, ClientThread> serverClients;
    private ServerSocket serverSocket;
    private final int serverPort;
    private InetAddress serverIP;
}
