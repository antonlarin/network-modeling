package networkmodeling.server;

public enum ServerCommandType {
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    
    GetFullNetworkModel,
    
    DropSenderConnection
}