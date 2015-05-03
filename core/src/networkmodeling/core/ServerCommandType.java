package networkmodeling.core;

public enum ServerCommandType {
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    ChangeDeviceIP,
    ChangeNICGateway,
    
    MoveGraphNode,
    
    GetFullNetworkModel,
    
    DropSenderConnection
}