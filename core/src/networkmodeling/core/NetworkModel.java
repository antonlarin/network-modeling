package networkmodeling.core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkModel implements Serializable {

    public NetworkModel()
    {
        networkDevices = new HashMap<>();
    }

    public boolean AddDevice(NetworkDevice dev)
    {
        if(!networkDevices.containsKey(dev.getMacAddress()))
        {
            networkDevices.put(dev.getMacAddress(), dev);
            return true;
        }
        
        return false;
    }

    public boolean ConnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        if(networkDevices.containsKey(dev1.getMacAddress()) &&
                networkDevices.containsKey(dev2.getMacAddress()))
        {
            try {
                
                NetworkDevice localDev1 = networkDevices.get(dev1.getMacAddress());
                NetworkDevice localDev2 = networkDevices.get(dev2.getMacAddress());
                localDev1.connectTo(localDev2);
                return true;
                
            } catch (NoFreePortsException ex) {
                Logger.getLogger(NetworkModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return false;
    }

    public boolean DisconnectDevices(NetworkDevice dev1, NetworkDevice dev2)
    {
        if(networkDevices.containsKey(dev1.getMacAddress()) &&
                networkDevices.containsKey(dev2.getMacAddress()))
        {
            NetworkDevice localDev1 = networkDevices.get(dev1.getMacAddress());
            NetworkDevice localDev2 = networkDevices.get(dev2.getMacAddress());
            localDev1.DisconnectFrom(localDev2);
            return true;
        }
        return false;
    }

    public boolean TestNetwork()
    {
        LinkedList<NIC> allNICs = new LinkedList<>();
        
        for(NetworkDevice dev : networkDevices.values())
        {
            if(dev instanceof NIC)
                allNICs.add((NIC)dev);
        }
        
        while(!allNICs.isEmpty())
        {
            NIC nicForTest = allNICs.pop();
            String data = new String("Test");
            
            Iterator<NIC> j = allNICs.iterator();
            while(j.hasNext())
            {
                NIC sender = j.next();
                sender.sendData(data, nicForTest.getIpAddress());
                if(!nicForTest.GetIncomingData().equals(data))
                    return false;
                nicForTest.sendData(data, sender.getIpAddress());
                if(!sender.GetIncomingData().equals(data))
                    return false;
            }
        }
        
        return false;
    }

    public boolean SendData(IpAddress sourceIP, Object data, IpAddress target)
    {
        NIC sourceDev = FindNICByIP(sourceIP);
        NIC targetDev = FindNICByIP(target);
        if(sourceDev != null && targetDev != null)
        {
            sourceDev.sendData(data, target);
            if (targetDev.GetIncomingData().equals(data))
                return true;
        }
        return false;
    }

    public boolean DeleteDevice(NetworkDevice dev)
    {
        if(networkDevices.containsKey(dev.getMacAddress()))
        {
            NetworkDevice devForDelete = networkDevices.get(dev.getMacAddress());
            devForDelete.DisconnectFromAll();
            networkDevices.remove(dev.getMacAddress());
            
            return true;
        }
        
        return false;
    }

    public boolean AreConnected(NetworkDevice dev1, NetworkDevice dev2)
    {
        return dev1.isConnectedTo(dev2);
    }

    public boolean HasDevice(NetworkDevice dev)
    {
        return networkDevices.containsKey(dev.getMacAddress());
    }

    public HashMap<MacAddress, NetworkDevice> getDevicesMap()
    {
        return networkDevices;
    }
    
    public boolean ChangeDeviceIP(NIC dev, IpAddress newIP)
    {
        if(networkDevices.containsKey(dev.getMacAddress()))
        {
            ((NIC)networkDevices.get(dev.getMacAddress())).setIpAderss(newIP);
            return true;
        }

        return false;
    }
    
    public NetworkDevice FindByMac(MacAddress address)
    {
        return networkDevices.get(address);
    }
    
    private NIC FindNICByIP(IpAddress adress)
    {
        for(NetworkDevice dev : networkDevices.values())
        {
            if(dev instanceof NIC)
                if(((NIC)dev).getIpAddress().equals(adress))
                    return (NIC)dev;
        }
        
        return null;
    }

    private final HashMap<MacAddress, NetworkDevice> networkDevices;
}