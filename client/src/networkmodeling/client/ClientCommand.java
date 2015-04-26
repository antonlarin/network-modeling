package networkmodeling.client;

import networkmodeling.core.NetworkDevice;

public class ClientCommand implements java.io.Serializable
{
    public ClientCommand(CliendCommandType _type, NetworkDevice[] args)
    {
        commandType = _type;
        arguments = args;
    }  
    
    private NetworkDevice[] arguments;
    private CliendCommandType commandType;

    public NetworkDevice[] getArguments() {
        return arguments;
    }

    public CliendCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CliendCommandType commandType) {
        this.commandType = commandType;
    }
}