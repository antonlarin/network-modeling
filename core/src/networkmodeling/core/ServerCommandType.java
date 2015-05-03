package networkmodeling.core;

public enum ServerCommandType {
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    ChangeDeviceIP,
    ChangeNICGateway,
    
    AddRoutingTableRecord,
    RemoveRoutingTableRecord,
    
    MoveGraphNode,
    
    GetFullNetworkModel,
    
    DropSenderConnection
}