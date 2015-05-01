package networkmodeling.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import networkmodeling.core.CliendCommandType;
import networkmodeling.core.ClientCommand;
import networkmodeling.core.GlobalConstants;
import networkmodeling.core.IpAddress;
import networkmodeling.core.NetworkVisualModel;
import networkmodeling.core.ServerCommand;
import networkmodeling.core.ServerCommandType;
import networkmodeling.core.modelgraph.NetworkGraphNode;
import networkmodeling.exceptions.ConnectionClosedException;
import networkmodeling.exceptions.NMException;

public class Client extends SwingWorker<Void, Void> {

    
    public Client()
    {
        networkModel = new NetworkVisualModel();
        isConnectedToServer = false;
        serverSocket = null;
        observers = new LinkedList<>();
        exceptionQueue = new LinkedList<>();
    }
    

    @Override
    protected Void doInBackground() {
        runServer();
        return null;
    }
    
    @Override
    protected void process(List<Void> chunks) {
        for (Observer o : observers) {
            o.update(null, null);
        }
    }
    
    
    public void runServer()
    {
        try {
            serverSocket = new Socket(InetAddress.getLocalHost(),
                    GlobalConstants.serverPort);
            outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            inputStream = new ObjectInputStream(serverSocket.getInputStream());
            isConnectedToServer = true;

            try {
                clientID = (UUID)inputStream.readObject();
                System.out.println("Connected\n");
                SendUpdateModelRequest();
                while(!isCancelled()) 
                {
                    ClientCommand command = (ClientCommand) inputStream.readObject();
                    if(command.getCommandType() != CliendCommandType.DropConnection)
                        executeCommand(command);
                    else
                    {
                        serverSocket.close();
                        System.out.println("Disconnected\n");
                        isConnectedToServer = false;
                        return;
                    }
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SocketException ex) {
            exceptionQueue.push(new ConnectionClosedException());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void executeCommand(ClientCommand command)
    {
        boolean isCommandExecuted = false;
        switch(command.getCommandType())
        {
            case AddDevice:
                isCommandExecuted = networkModel.AddDevice(
                        (NetworkGraphNode)command.getArguments()[0]);
                publish();
                break;
            case DeleteDevice:
                isCommandExecuted = networkModel.DeleteDevice(
                        (NetworkGraphNode)command.getArguments()[0]);
                publish();
                break;
            case ConnectDevices:
                isCommandExecuted = networkModel.ConnectDevices(
                        (NetworkGraphNode)command.getArguments()[0],
                        (NetworkGraphNode)command.getArguments()[1]);
                publish();
                break;
            case DisconnectDevices:
                isCommandExecuted = networkModel.DisconnectDevices(
                        (NetworkGraphNode)command.getArguments()[0],
                        (NetworkGraphNode)command.getArguments()[1]);
                publish();
                break;
            case ChangeDeviceIP:
                isCommandExecuted = networkModel.ChangeDeviceIP(
                        (NetworkGraphNode)(command.getArguments()[0]),
                        (IpAddress)(command.getArguments()[1]));
                publish();
                break;
            case MoveGraphNode:
                isCommandExecuted = networkModel.GetGraph().ChangeNodeCoordinates(
                        (NetworkGraphNode)command.getArguments()[0],
                        (double)command.getArguments()[1],
                        (double)command.getArguments()[2]);
                publish();
                break;
            case UpdateFullModel:
                networkModel = (NetworkVisualModel)command.getArguments()[0];
                isCommandExecuted = true;
                publish();
                break;
        }
        
        if(!isCommandExecuted)
            SendUpdateModelRequest();
    }
    
    public boolean isConnectedToServer()
    {
        return isConnectedToServer;
    }
    
    public void connectToServer()
    {
        this.execute();
    }
    
    public void disconnect()
    {
        if(isConnectedToServer)
        {
            try {
                outputStream.writeObject(new ServerCommand(ServerCommandType.DropSenderConnection, null));
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.cancel(true);
            try {
                serverSocket.close();
                isConnectedToServer = false;
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Disconnected\n");
        }
    }
    
    public void SendConnectDevicesRequest(NetworkGraphNode dev1, NetworkGraphNode dev2)
    {
        if(isConnectedToServer)
        {
            Object args[] = new NetworkGraphNode[2];
            args[0] = dev1;
            args[1] = dev2;

            ServerCommand command = new ServerCommand(
                    ServerCommandType.ConnectDevices, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void SendChangeDeviceIPRequest(NetworkGraphNode dev, IpAddress newIP)
    {
        if(isConnectedToServer)
        {
            Object args[] = new NetworkGraphNode[2];
            args[0] = dev;
            args[1] = (Object)newIP;

            ServerCommand command = new ServerCommand(
                    ServerCommandType.ChangeDeviceIP, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void SendDisonnectDevicesRequest(NetworkGraphNode dev1, NetworkGraphNode dev2)
    {
        if(isConnectedToServer)
        {
            Object args[] = new NetworkGraphNode[2];
            args[0] = dev1;
            args[1] = dev2;

            ServerCommand command = new ServerCommand(
                    ServerCommandType.DisconnectDevices, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void SendDeleteDeviceRequest(NetworkGraphNode dev1)
    {
        if(isConnectedToServer)
        {
            Object args[] = new NetworkGraphNode[1];
            args[0] = dev1;

            ServerCommand command = new ServerCommand(
                    ServerCommandType.DeleteDevice, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void SendAddDevicesRequest(NetworkGraphNode dev1)
    {
        if(isConnectedToServer)
        {
            Object args[] = new NetworkGraphNode[1];
            args[0] = dev1;

            ServerCommand command = new ServerCommand(
                ServerCommandType.AddDevice, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void SendСhangeNodeCoordinatesRequest(NetworkGraphNode dev, double x, double y)
    {
        if(isConnectedToServer)
        {
            Object args[] = new Object[3];
            args[0] = dev;
            args[1] = x;
            args[2] = y;

            ServerCommand command = new ServerCommand(
                ServerCommandType.MoveGraphNode, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void LoadModelFromServer()
    {
        SendUpdateModelRequest();
    }
    
    public NetworkVisualModel GetVisualModel()
    {
        return networkModel;
    }
    
    public void addObserver(Observer o) {
        observers.add(o);
    }
    
    private void SendUpdateModelRequest()
    {
        if(isConnectedToServer)
            try {
                outputStream.writeObject(new ServerCommand(ServerCommandType.GetFullNetworkModel, null));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private NetworkVisualModel networkModel;
    private boolean isConnectedToServer;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket serverSocket;
    private UUID clientID;
    private LinkedList<Observer> observers;
    private LinkedList<NMException> exceptionQueue;
}
