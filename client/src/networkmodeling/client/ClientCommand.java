package networkmodeling.client;

public class ClientCommand implements java.io.Serializable
{
    public ClientCommand(CliendCommandType _type, Object[] args)
    {
        commandType = _type;
        arguments = args;
    }  
    
    private Object[] arguments;
    private CliendCommandType commandType;

    public Object[] getArguments() {
        return arguments;
    }

    public CliendCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CliendCommandType commandType) {
        this.commandType = commandType;
    }
}
