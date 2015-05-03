package networkmodeling.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import networkmodeling.client.ui.ClientAppModel;
import networkmodeling.core.CliendCommandType;
import networkmodeling.core.ClientCommand;
import networkmodeling.core.GlobalConstants;
import networkmodeling.core.IpAddress;
import networkmodeling.core.NetworkVisualModel;
import networkmodeling.core.RoutingTableRecord;
import networkmodeling.core.ServerCommand;
import networkmodeling.core.ServerCommandType;
import networkmodeling.core.modelgraph.NetworkGraphNode;
import networkmodeling.exceptions.ConnectionClosedException;
import networkmodeling.exceptions.NMException;
import networkmodeling.exceptions.NoFreePortsException;

public class ClientDaemon extends SwingWorker<Void, Void> {


    public ClientDaemon(ClientAppModel model)
    {
        clientAppModel = model;
        isConnectedToServer = false;
        serverSocket = null;
        exceptionQueue = new LinkedList<>();
    }


    @Override
    protected Void doInBackground() {
        runServer();
        return null;
    }

    @Override
    protected void process(List<Void> chunks) {
        clientAppModel.setVisualModelChanged();
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
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SocketException ex) {
            exceptionQueue.push(new ConnectionClosedException());
        } catch (IOException ex) {
            Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executeCommand(ClientCommand command)
    {
        boolean isCommandExecuted = false;
        switch(command.getCommandType())
        {
            case AddDevice:
                isCommandExecuted = clientAppModel.getVisualModel().AddDevice(
                        (NetworkGraphNode)command.getArguments()[0]);
                break;
            case DeleteDevice:
                isCommandExecuted = clientAppModel.getVisualModel().DeleteDevice(
                        (NetworkGraphNode)command.getArguments()[0]);
                break;
            case ConnectDevices:
                try {
                    isCommandExecuted = clientAppModel.getVisualModel().ConnectDevices(
                        (NetworkGraphNode)command.getArguments()[0],
                        (NetworkGraphNode)command.getArguments()[1]);
                } catch (NoFreePortsException ex) {
                    isCommandExecuted = false;
                }
                break;
            case DisconnectDevices:
                isCommandExecuted = clientAppModel.getVisualModel().DisconnectDevices(
                        (NetworkGraphNode)command.getArguments()[0],
                        (NetworkGraphNode)command.getArguments()[1]);
                break;
            case ChangeDeviceIP:
                isCommandExecuted = clientAppModel.getVisualModel().ChangeDeviceIP(
                        (NetworkGraphNode)(command.getArguments()[0]),
                        (IpAddress)(command.getArguments()[1]));
                break;
            case ChangeNICGateway:
                isCommandExecuted = clientAppModel.getVisualModel().ChangeNICGateway(
                        (NetworkGraphNode)(command.getArguments()[0]),
                        (IpAddress)(command.getArguments()[1]));
                break;
            case AddRoutingTableRecord:
                isCommandExecuted = clientAppModel.getVisualModel().AddRoutingTableRecord(
                        (NetworkGraphNode)(command.getArguments()[0]),
                        (RoutingTableRecord)(command.getArguments()[1]));
                break;
            case RemoveRoutingTableRecord:
                isCommandExecuted = clientAppModel.getVisualModel().DeleteRoutingTableRecord(
                        (NetworkGraphNode)(command.getArguments()[0]),
                        (RoutingTableRecord)(command.getArguments()[1]));
                break;
            case MoveGraphNode:
                isCommandExecuted = clientAppModel.getVisualModel().GetGraph().ChangeNodeCoordinates(
                        (NetworkGraphNode)command.getArguments()[0],
                        (double)command.getArguments()[1],
                        (double)command.getArguments()[2]);
                break;
            case UpdateFullModel:
                clientAppModel.setVisualModel(
                    (NetworkVisualModel)command.getArguments()[0]);
                isCommandExecuted = true;
                break;
        }

        if(isCommandExecuted)
            publish();
        else
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
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.cancel(true);
            try {
                serverSocket.close();
                isConnectedToServer = false;
            } catch (IOException ex) {
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void SendChangeDeviceIPRequest(NetworkGraphNode dev, IpAddress newIP)
    {
        if(isConnectedToServer)
        {
            Object args[] = new Object[2];
            args[0] = dev;
            args[1] = newIP;

            ServerCommand command = new ServerCommand(
                    ServerCommandType.ChangeDeviceIP, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void SendChangeNICGatewayRequest(NetworkGraphNode dev, IpAddress newGatewayIP)
    {
        if(isConnectedToServer)
        {
            Object args[] = new Object[2];
            args[0] = dev;
            args[1] = newGatewayIP;

            ServerCommand command = new ServerCommand(
                    ServerCommandType.ChangeNICGateway, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void SendDisconnectDevicesRequest(NetworkGraphNode dev1, NetworkGraphNode dev2)
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
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void SendRemoveRoutingTableRecorgRequest(NetworkGraphNode dev,
            RoutingTableRecord record)
    {
        if(isConnectedToServer)
        {
            Object args[] = new Object[2];
            args[0] = dev;
            args[1] = record;

            ServerCommand command = new ServerCommand(
                ServerCommandType.RemoveRoutingTableRecord, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void SendAddRoutingTableRecorgRequest(NetworkGraphNode dev,
            RoutingTableRecord record)
    {
        if(isConnectedToServer)
        {
            Object args[] = new Object[2];
            args[0] = dev;
            args[1] = record;

            ServerCommand command = new ServerCommand(
                ServerCommandType.AddRoutingTableRecord, args);
            try {
                outputStream.writeObject(command);
            } catch (IOException ex) {
                Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void LoadModelFromServer()
    {
        SendUpdateModelRequest();
    }

    private void SendUpdateModelRequest()
    {
        if(isConnectedToServer)
            try {
                outputStream.writeObject(new ServerCommand(ServerCommandType.GetFullNetworkModel, null));
        } catch (IOException ex) {
            Logger.getLogger(ClientDaemon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private final ClientAppModel clientAppModel;
    private boolean isConnectedToServer;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket serverSocket;
    private UUID clientID;
    private LinkedList<NMException> exceptionQueue;
}
