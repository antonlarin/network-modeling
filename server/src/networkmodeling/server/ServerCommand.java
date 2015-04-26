package networkmodeling.server;

import networkmodeling.core.NetworkDevice;

public class ServerCommand implements java.io.Serializable {

    public ServerCommand() {

    }
    public ServerCommand(ServerCommandType _commandType, NetworkDevice[] _args) {
        commandType = _commandType;
        commandArgs = _args;
    }

    public ServerCommandType getCommandType() {
        return commandType;
    }

    public NetworkDevice[] getCommandArgs() {
        return commandArgs;
    }

    private ServerCommandType commandType;
    private NetworkDevice[] commandArgs;
}