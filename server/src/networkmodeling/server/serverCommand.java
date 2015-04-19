package networkmodeling.server;

import java.util.UUID;

enum ServerCommands {
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    
    GetFullNetworkModel,
    
    DropSenderConnection
}

public class serverCommand {
    
    public serverCommand(ServerCommands _commandType,
            Object[] _args)
    {
        commandType = _commandType;
        commandArgs = _args;
    }

    public ServerCommands getCommandType() {
        return commandType;
    }

    public Object[] getCommandArgs() {
        return commandArgs;
    }
    
    private ServerCommands commandType;
    private Object[] commandArgs; 
}