package networkmodeling.server;

import java.net.UnknownHostException;

public class ServerModel {
    
    public ServerModel() throws UnknownHostException
    {
        serversLogs = new String();
        server = new Server();
    }
    
    public void startServer() throws UnknownHostException
    {
        server = new Server();
        server.startServer();
    }
    public void stopServer()
    {
        server.stopServer();
        serversLogs += server.getServerLog();
    }
    
    public String getServerLog()
    {
        return serversLogs + server.getServerLog();
    }
    
    private String serversLogs;
    private Server server;
}
