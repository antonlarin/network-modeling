package networkmodeling.core;

import java.io.Serializable;
import networkmodeling.core.modelgraph.NetworkGraphNode;
import networkmodeling.core.modelgraph.NetworkModelGraph;
import networkmodeling.exceptions.NoFreePortsException;

public class NetworkVisualModel implements Serializable {

    public NetworkVisualModel()
    {
        model = new NetworkModel();
        graph = new NetworkModelGraph();
    }

    public boolean AddDevice(NetworkGraphNode dev)
    {
        return model.AddDevice(dev.getNodeDevice()) &&
                graph.AddDevice(dev);
    }
    public boolean ConnectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2)
        throws NoFreePortsException
    {
        return model.ConnectDevices(dev1.getNodeDevice(), dev2.getNodeDevice()) &&
                graph.ConnectDevices(dev1, dev2);
    }
    public boolean DisconnectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2)
    {
        return  model.DisconnectDevices(dev1.getNodeDevice(), dev2.getNodeDevice()) &&
                graph.DisconnectDevices(dev1, dev2);

    }
    public boolean DeleteDevice(NetworkGraphNode dev)
    {
        return  model.DeleteDevice(dev.getNodeDevice()) &&
                graph.DeleteDevice(dev);
    }
    public boolean ChangeDeviceIP(NetworkGraphNode dev, IpAddress newIP)
    {
        return  model.ChangeDeviceIP((IpBasedNetworkDevice)dev.getNodeDevice(), newIP) &&
                graph.ChangeDeviceIP(dev, newIP);
    }
    public boolean AddRoutingTableRecord(NetworkGraphNode dev, RoutingTableRecord record)
    {
        return model.AddRoutingTableRecord((Router)dev.getNodeDevice(), record);
    }
    public boolean DeleteRoutingTableRecord(NetworkGraphNode dev, RoutingTableRecord record)
    {
        return model.DeleteRoutingTableRecord((Router)dev.getNodeDevice(), record);
    }
    public boolean setRoutingTable(NetworkGraphNode dev, RoutingTable table)
    {
        return model.SetRoutingTable((Router)dev.getNodeDevice(), table);
    }
    public boolean ChangeNICGateway(NetworkGraphNode dev, IpAddress newGatewayIP)
    {
        return model.ChangeNICGateway((NIC)dev.getNodeDevice(), newGatewayIP);
    }

    public NetworkModel GetModel() {
        return model;
    }

    public NetworkModelGraph GetGraph() {
        return graph;
    }

    private final NetworkModel model;
    private final NetworkModelGraph graph;
}
