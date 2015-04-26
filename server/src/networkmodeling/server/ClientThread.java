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
            //outputStream.defaultWriteObject(client);
            connectionSocket.close();
            this.stop();
        } catch (IOException except) {
          
        }
    }
    
    private synchronized void executeClientCommand(ServerCommand command) throws Exception
    {
        boolean isCommandExecuted = false;
        switch (command.getCommandType())
        {
            case AddDevice:
                parentServer.GetModel().AddDevice(command.getCommandArgs()[0]);
                isCommandExecuted = true;
                break;
            case DeleteDevice:
                isCommandExecuted = parentServer.GetModel().DeleteDevice(command.getCommandArgs()[0]);
                break;
            case ConnectDevices:
                isCommandExecuted = parentServer.GetModel().ConnectDevices(
                        command.getCommandArgs()[0], command.getCommandArgs()[1]);
                break;
            case DisconnectDevices:
                isCommandExecuted = parentServer.GetModel().DisconnectDevices(
                        command.getCommandArgs()[0], command.getCommandArgs()[1]);
                break;
            case GetFullNetworkModel:
                UpdateClientModel();
                isCommandExecuted = true;
                break;
        }
        
        if(command.getCommandType() != ServerCommandType.GetFullNetworkModel)
            parentServer.BroadcastChanges(command);
        
        if(!isCommandExecuted)
            UpdateClientModel();
    }
    
    private void UpdateClientModel()
    {
        
    }
    
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket connectionSocket;
    private Server parentServer;
    private UUID clientID;
}
