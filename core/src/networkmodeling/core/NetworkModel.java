package networkmodeling.core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkModel implements Serializable {
    
    public NetworkModel()
    {
        devices = new LinkedList<>();
    }
    
    public void AddDevice(NetworkDevice dev)
    {
        devices.add(dev);
    }
    
    public boolean ConnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        if (devices.contains(dev2) && devices.contains(dev1))
        {
            try {
                dev1.connectTo(dev2);
                devices.remove(dev1);
                devices.remove(dev2);
                devices.add(dev1);
                devices.add(dev2);
                return true;
            } catch (Exception ex) {
                Logger.getLogger(NetworkModel.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
    
    public boolean DisconnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        return false;
    }
    
    public boolean TestNetwork()
    {
        return false;
    }
    
    public boolean DeleteDevice(NetworkDevice dev)
    {
        Iterator<NetworkDevice> i = devices.iterator();
        while(i.hasNext())
        {
            if(i.next().equals(dev))
            {
                i.remove();
                return true;
            }
        }
        return false;
    }
    
    public boolean IsConnected(NetworkDevice dev1, NetworkDevice dev2)
    {
        return false;
    }
    
    public boolean HasDevice(NetworkDevice dev)
    {
        return devices.contains(dev);
    }
    
    public LinkedList<NetworkDevice> getDevicesList()
    {
        return devices;
    }
    
    private final LinkedList<NetworkDevice> devices;
}