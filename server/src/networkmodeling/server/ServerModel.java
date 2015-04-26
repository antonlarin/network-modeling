package networkmodeling.server;

import java.net.UnknownHostException;

public class ServerModel {
    
    public ServerModel(javax.swing.JTextPane logView) throws UnknownHostException
    {
        serversLogs = new String();
        server = new Server(logView);
        serverLogView = logView;
    }
    
    public void startServer() throws UnknownHostException
    {
        server = new Server(serverLogView);
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
    
    private final javax.swing.JTextPane serverLogView;
    private String serversLogs;
    private Server server;
}
