package networkmodeling.server;

import networkmodeling.core.ServerCommand;
import networkmodeling.core.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Server extends Thread{
    public Server(javax.swing.JTextPane _logViewPanel)
    {
        serverSocket = null;
        serverIP = null;
        serverPort = GlobalConstants.serverPort;
        serverClients = new HashMap<>();
        eventsLog = new String();
        logViewPanel = _logViewPanel;
        mainNetworkModel = new NetworkVisualModel();
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
                eventsLog += "Клиент подключился.\n";
                logViewPanel.setText(eventsLog);
                client.start();
            }
        }
        catch(IOException excep)    {
            
        }
    }
    
    public void startServer()
    {
        eventsLog += "Сервер запущен.\n";
        logViewPanel.setText(eventsLog);
        this.start();
    }
    public void stopServer()
    {
        eventsLog += "Сервер остановлен.\n";
        logViewPanel.setText(eventsLog);
        try {         
           
            Iterator<ClientThread> i = serverClients.values().iterator();
            while (i.hasNext())
                i.next().close();
            
            serverSocket.close(); 
            serverClients.clear();
            this.stop();
        } catch (IOException ex) {
            
        }
    }
    public void dropClient(UUID clientID)
    {
        ClientThread clientForDrop = serverClients.get(clientID);
        eventsLog += "Клиент отключился.\n";
            logViewPanel.setText(eventsLog);
        if(clientForDrop != null)
        { 
            clientForDrop.close();
            serverClients.remove(clientID);
        }
    }
    public NetworkVisualModel GetModel()
    {
        return mainNetworkModel;
    }
    
    public void BroadcastChanges(ServerCommand command, UUID sourceID)
    {
        for(ClientThread value : serverClients.values())
        {
            if(!sourceID.equals(value.getUUID()))
            {
                ClientCommand updateCommand = null;
                switch(command.getCommandType())
                {
                    case AddDevice:
                        updateCommand = new ClientCommand(
                                CliendCommandType.AddDevice,
                                command.getCommandArgs());
                        break;
                    case ChangeDeviceIP:
                        updateCommand = new ClientCommand(
                                CliendCommandType.ChangeDeviceIP,
                                command.getCommandArgs());
                        break;
                    case ChangeNICGateway:
                        updateCommand = new ClientCommand(
                                CliendCommandType.ChangeNICGateway,
                                command.getCommandArgs());
                        break;
                    case ConnectDevices:
                        updateCommand = new ClientCommand(
                                CliendCommandType.ConnectDevices,
                                command.getCommandArgs());
                        break;
                    case DeleteDevice:
                        updateCommand = new ClientCommand(
                                CliendCommandType.DeleteDevice,
                                command.getCommandArgs());
                        break;
                    case DisconnectDevices:
                        updateCommand = new ClientCommand(
                                CliendCommandType.DisconnectDevices,
                                command.getCommandArgs());
                        break;
                    case MoveGraphNode:
                        updateCommand = new ClientCommand(
                                CliendCommandType.MoveGraphNode,
                                command.getCommandArgs());
                        break;
                }
                
                value.SendCommand(updateCommand);
            }
        }
    }
    
    public String getServerLog()
    {
        return eventsLog;
    }
    
    private final javax.swing.JTextPane logViewPanel;
    
    private final NetworkVisualModel mainNetworkModel;
    
    private String eventsLog;
    private final HashMap<UUID, ClientThread> serverClients;
    private ServerSocket serverSocket;
    private final int serverPort;
    private InetAddress serverIP;
}
