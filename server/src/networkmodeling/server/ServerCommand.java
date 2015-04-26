package networkmodeling.server;

public class ServerCommand implements java.io.Serializable {

    public ServerCommand() {

    }
    public ServerCommand(ServerCommandType _commandType, Object[] _args) {
        commandType = _commandType;
        commandArgs = _args;
    }

    public ServerCommandType getCommandType() {
        return commandType;
    }

    public Object[] getCommandArgs() {
        return commandArgs;
    }

    private ServerCommandType commandType;
    private Object[] commandArgs;
}