package networkmodeling.client;

import networkmodeling.core.ServerCommandType;
import networkmodeling.core.ServerCommand;
import networkmodeling.core.CliendCommandType;
import networkmodeling.core.ClientCommand;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import networkmodeling.core.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import networkmodeling.server.*;

public class Client extends Thread {

    
    public Client()
    {
        networkModel = new NetworkModel();
        isConnectedToServer = false;
        serverSocket = null;
    }
    
    
    @Override
    public void run()
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
                while(true) 
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
                networkModel.AddDevice(command.getArguments()[0]);
                break;
            case DeleteDevice:
                networkModel.DeleteDevice(command.getArguments()[0]);
                break;
            case ConnectDevices:
                networkModel.ConnectDevices(
                        command.getArguments()[0],
                        command.getArguments()[1]);
                break;
            case DisconnectDevices:
                networkModel.DisconnectDevices(
                        command.getArguments()[0],
                        command.getArguments()[1]);
                break;
            case UpdateFullModel:
        {
            try {
                networkModel = (NetworkModel)inputStream.readObject();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        this.start();
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
            this.stop();
            try {
                serverSocket.close();
                isConnectedToServer = false;
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Disconnected\n");
        }
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
    
    private NetworkModel networkModel;
    private boolean isConnectedToServer;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket serverSocket;
    private NetworkModel model;
    private UUID clientID;
}
