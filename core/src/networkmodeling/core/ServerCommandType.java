package networkmodeling.core;

public enum ServerCommandType {
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    ChangeDeviceIP,
    MoveGraphNode,
    
    GetFullNetworkModel,
    
    DropSenderConnection
}