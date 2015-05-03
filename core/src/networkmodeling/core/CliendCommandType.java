package networkmodeling.core;

public enum CliendCommandType
{
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
    
    UpdateFullModel,
    
    DropConnection
}
