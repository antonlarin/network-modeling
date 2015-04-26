package networkmodeling.core;

public enum ServerCommandType {
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    
    GetFullNetworkModel,
    
    DropSenderConnection
}