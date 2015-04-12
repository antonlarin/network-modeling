package networkmodeling.server;

import java.net.Socket;
import java.util.UUID;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
            outputStream = new DataOutputStream(
                    connectionSocket.getOutputStream());
            inputStream = new DataInputStream(
                    connectionSocket.getInputStream());
            
            while (true) {
                int comand = inputStream.read();
                switch(comand){
                    case 0:
                        return;
                    case 1: 
                        return;
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
    
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Socket connectionSocket;
    private Server parentServer;
    private UUID clientID;
}
