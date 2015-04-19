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
                serverCommand command;
                try {
                    command = (serverCommand)inputStream.readObject();
                    if(command.getCommandType() != ServerCommands.DropSenderConnection)
                        executeClientCommand(command);
                    else{
                        parentServer.dropClient(clientID);
                        break;
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
            connectionSocket.close();
            this.stop();
        } catch (IOException except) {
          
        }
    }
    
    private void executeClientCommand(serverCommand command)
    {
        
    }
    
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket connectionSocket;
    private Server parentServer;
    private UUID clientID;
}
