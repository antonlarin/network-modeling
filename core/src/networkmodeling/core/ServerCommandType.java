package networkmodeling.core;

public enum ServerCommandType {
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    ChangeDeviceIP,
    ChangeNICGateway,
    
    SetRoutingTable,
    AddRoutingTableRecord,
    RemoveRoutingTableRecord,
    
    MoveGraphNode,
    
    GetFullNetworkModel,
    
    DropSenderConnection
}