package networkmodeling.core;

public enum CliendCommandType
{
    AddDevice,
    DeleteDevice,
    ConnectDevices,
    DisconnectDevices,
    ChangeDeviceIP,
    ChangeNICGateway,
    
    AddRoutingTableRecord,
    RemoveRoutingTableRecord,
    
    MoveGraphNode,
    
    UpdateFullModel,
    
    DropConnection
}
