package networkmodeling.core;

import java.util.Iterator;
import java.util.LinkedList;

public class NetworkModel {
    
    public NetworkModel()
    {
        devices = new LinkedList<>();
    }
    
    void AddDevice(NetworkDevice dev)
    {
        devices.add(dev);
    }
    
    void ConnectDevices(NetworkDevice dev1, NetworkDevice dev2) throws Exception
    {
        dev1.connectTo(dev2);
    }
    
    void DisconnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        
    }
    
    void DeleteDevice(NetworkDevice dev)
    {
        Iterator<NetworkDevice> i = devices.iterator();
        while(i.hasNext())
        {
            if(i.next().equals(dev))
            {
                i.remove();
                break;
            }
        }
    }
    
    LinkedList<NetworkDevice> getDevicesList()
    {
        return devices;
    }
    
    private final LinkedList<NetworkDevice> devices;
}