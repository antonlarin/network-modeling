package networkmodeling.server;

import networkmodeling.core.ServerCommand;
import networkmodeling.core.ServerCommandType;
import java.net.Socket;
import java.util.UUID;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkmodeling.core.CliendCommandType;
import networkmodeling.core.ClientCommand;
import networkmodeling.core.IpAddress;
import networkmodeling.core.NIC;
import networkmodeling.core.NetworkDevice;

public class ClientThread extends Thread{
    
    public ClientThread()
    {
        
    }
    public ClientThread(Server _server, Socket _socket)
    {
        clientID = UUID.randomUUID();
        parentServer = _server;
        connectionSocket = _socket;
    }
    public UUID getUUID(){
        return clientID;
    }
    
    @Override
    public void run()
    {
        try {
            outputStream = new ObjectOutputStream(
                    connectionSocket.getOutputStream());
            inputStream = new ObjectInputStream(
                    connectionSocket.getInputStream());
            
            outputStream.writeObject(clientID);
            UpdateClientModel();
            
            while (true) {
                ServerCommand command;
                try {
                    command = (ServerCommand)inputStream.readObject();
                    
                    if(command.getCommandType() != ServerCommandType.DropSenderConnection)
                        executeClientCommand(command);
                    else{
                        parentServer.dropClient(clientID);
                        return;
                    }
                        
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }        
        } catch (IOException ex) {
            
        }
    }
    public void close()
    {
        try {
            outputStream.writeObject(new ClientCommand(CliendCommandType.DropConnection, null));
            connectionSocket.close();
            this.stop();
        } catch (IOException except) {
          
        }
    }
    
    private synchronized void executeClientCommand(ServerCommand command) throws Exception
    {
        boolean isCommandExecuted = false;
        System.out.println("Start execuntion \n");
        switch (command.getCommandType())
        {
            case AddDevice:
                parentServer.GetModel().AddDevice((NetworkDevice)
                        command.getCommandArgs()[0]);
                isCommandExecuted = true;
                break;
            case DeleteDevice:
                isCommandExecuted = parentServer.GetModel().DeleteDevice(
                        (NetworkDevice)command.getCommandArgs()[0]);
                break;
            case ConnectDevices:
                isCommandExecuted = parentServer.GetModel().ConnectDevices(
                        (NetworkDevice)command.getCommandArgs()[0],
                        (NetworkDevice)command.getCommandArgs()[1]);
                break;
            case DisconnectDevices:
                isCommandExecuted = parentServer.GetModel().DisconnectDevices(
                        (NetworkDevice)command.getCommandArgs()[0],
                        (NetworkDevice)command.getCommandArgs()[1]);
                break;
            case ChangeDeviceIP:
                isCommandExecuted = parentServer.GetModel().ChangeDeviceIP(
                        (NIC)command.getCommandArgs()[0],
                        (IpAddress)command.getCommandArgs()[1]);
                break;
            case GetFullNetworkModel:
                UpdateClientModel();
                isCommandExecuted = true;
                break;
        }
        
        if(command.getCommandType() != ServerCommandType.GetFullNetworkModel)
            parentServer.BroadcastChanges(command, clientID);
        
        if(!isCommandExecuted)
            UpdateClientModel();
    }
    
    public void UpdateClientModel()
    {
        Object args[] = new Object[1];
        args[0] = parentServer.GetModel();
        ClientCommand updateCommand = new ClientCommand(
                CliendCommandType.UpdateFullModel, args);
        try {
            outputStream.writeObject(updateCommand);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void SendCommand(ClientCommand command)
    {
        try {
            outputStream.writeObject(command);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket connectionSocket;
    private Server parentServer;
    private UUID clientID;
}
