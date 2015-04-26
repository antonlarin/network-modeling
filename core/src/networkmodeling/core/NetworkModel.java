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
                
                NetworkDevice localDev1 = FindByMac(dev1.getMacAddress());
                NetworkDevice localDev2 = FindByMac(dev2.getMacAddress());
                localDev1.connectTo(localDev2);
                
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
        if(devices.contains(dev2) && devices.contains(dev1))
        {
            NetworkDevice localDev1 = FindByMac(dev1.getMacAddress());
            NetworkDevice localDev2 = FindByMac(dev2.getMacAddress());
            localDev1.DisconnectFrom(localDev2);
            
            return true;
        }
        return false;
    }

    public boolean TestNetwork()
    {
        return false;
    }

    public boolean SendData(IpAddress sourceIP, Object data, IpAddress target)
    {
        NIC sourceDev = FindNICByIP(sourceIP);
        if(sourceDev != null)
        {
            sourceDev.sendData(data, target);
            return true;
        }
        return false;
    }

    public boolean DeleteDevice(NetworkDevice dev)
    {
        Iterator<NetworkDevice> i = devices.iterator();
        while(i.hasNext())
        {
            NetworkDevice devForDelete = i.next();
            if(devForDelete.equals(dev))
            {
                devForDelete.DisconnectFromAll();
                i.remove();
                return true;
            }
            
        }
        return false;
    }

    public boolean AreConnected(NetworkDevice dev1, NetworkDevice dev2)
    {
        return dev1.isConnectedTo(dev2);
    }

    public boolean HasDevice(NetworkDevice dev)
    {
        return devices.contains(dev);
    }

    public LinkedList<NetworkDevice> getDevicesList()
    {
        return devices;
    }

    private NIC FindNICByIP(IpAddress adress)
    {
        Iterator<NetworkDevice> i = devices.iterator();
        while(i.hasNext())
        {
            NetworkDevice currentDev = i.next();

            if(currentDev != null && currentDev instanceof NIC)
            {
                if(((NIC)currentDev).getIpAddress().equals(adress))
                    return (NIC)currentDev;
            }
        }
        return null;
    }
    
    public NetworkDevice FindByMac(MacAddress adress)
    {
        Iterator<NetworkDevice> i = devices.iterator();
        while(i.hasNext())
        {
            NetworkDevice currentDev = i.next();
            if(currentDev.getMacAddress().equals(adress))
                return currentDev;
        }
        return null;
    }

    private final LinkedList<NetworkDevice> devices;
}