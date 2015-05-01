package networkmodeling.core;

import java.io.Serializable;


public class NetworkModelGraph implements Serializable {
    
    
    public boolean AddDevice(NetworkDevice dev)
    {
        return true;
    }
    public boolean ConnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
         return true;
    }
    public boolean DisconnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        return true;
    }
    public boolean DeleteDevice(NetworkDevice dev)
    {
        return true;
    }
    public boolean ChangeDeviceIP(NIC dev, IpAddress newIP)
    {
        return true;
    }
    
    
}
