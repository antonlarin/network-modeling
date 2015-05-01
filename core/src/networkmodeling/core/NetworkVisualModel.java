package networkmodeling.core;

import java.io.Serializable;

public class NetworkVisualModel implements Serializable {
    
    public NetworkVisualModel()
    {
        model = new NetworkModel();
        graph = new NetworkModelGraph();
    }
    
    public boolean AddDevice(NetworkDevice dev)
    {
        return model.AddDevice(dev) &&
                graph.AddDevice(dev);
    }
    public boolean ConnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        return model.ConnectDevices(dev1, dev2) &&
                graph.ConnectDevices(dev1, dev2);
    }
    public boolean DisconnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        return  model.ConnectDevices(dev1, dev2) &&
                graph.DisconnectDevices(dev1, dev2);
        
    }
    public boolean DeleteDevice(NetworkDevice dev)
    {
        return  model.DeleteDevice(dev) &&
                graph.DeleteDevice(dev);
    }
    public boolean ChangeDeviceIP(NIC dev, IpAddress newIP)
    {
        return  model.ChangeDeviceIP(dev, newIP) &&
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
