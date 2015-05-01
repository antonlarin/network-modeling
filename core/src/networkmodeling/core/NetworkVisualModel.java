package networkmodeling.core;

import networkmodeling.core.modelgraph.*;
import java.io.Serializable;

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
    {
        return model.ConnectDevices(dev1.getNodeDevice(), dev2.getNodeDevice()) &&
                graph.ConnectDevices(dev1, dev2);
    }
    public boolean DisconnectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2)
    {
        return  model.ConnectDevices(dev1.getNodeDevice(), dev2.getNodeDevice()) &&
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
   
    
    public NetworkModel GetModel() {
        return model;
    }

    public NetworkModelGraph GetGraph() {
        return graph;
    }
    
    private final NetworkModel model;
    private final NetworkModelGraph graph;
}
