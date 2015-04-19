package networkmodeling.server;

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
    
    private void executeClientCommand(ServerCommand command)
    {
        
    }
    
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket connectionSocket;
    private Server parentServer;
    private UUID clientID;
}
