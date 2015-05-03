package networkmodeling.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import networkmodeling.exceptions.NoFreePortsException;

public class NetworkModel implements Serializable {

    public NetworkModel()
    {
        networkDevices = new HashMap<>();
        lastAddedAddress = new IpAddress("192.168.0.99");
    }

    public boolean AddDevice(NetworkDevice dev)
    {
        if(!networkDevices.containsKey(dev.getMacAddress()))
        {
            networkDevices.put(dev.getMacAddress(), dev);
            if(dev.getType() == NetworkDeviceType.NIC)
                lastAddedAddress = ((IpBasedNetworkDevice)dev).getIpAddress();
            return true;
        }

        return false;
    }

    public boolean ConnectDevices(NetworkDevice dev1, NetworkDevice dev2)
        throws NoFreePortsException
    {
        if(networkDevices.containsKey(dev1.getMacAddress()) &&
                networkDevices.containsKey(dev2.getMacAddress()))
        {
                NetworkDevice localDev1 = networkDevices.get(dev1.getMacAddress());
                NetworkDevice localDev2 = networkDevices.get(dev2.getMacAddress());
                localDev1.connectTo(localDev2);
                return true;
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

    public NetworkModelTestResult TestNetwork()
    {
        LinkedList<NIC> allNICs = new LinkedList<>();
        LinkedList<LinkedList<NetworkDevice>> routes = new LinkedList<>();

        for(NetworkDevice dev : networkDevices.values())
        {
            if(dev.getType() == NetworkDeviceType.NIC)
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
                routes.add(nicForTest.getLastIncomingDataRoute());

                if(!nicForTest.GetIncomingData().equals(data))
                {
                    LinkedList<NetworkDevice> failedRoute = 
                            new LinkedList<>();
                    failedRoute.add(sender);
                    failedRoute.add(nicForTest);
                    return new NetworkModelTestResult(false, routes, failedRoute);
                }

                nicForTest.sendData(data, sender.getIpAddress());
                routes.add(sender.getLastIncomingDataRoute());

                if(!sender.GetIncomingData().equals(data))
                {
                    LinkedList<NetworkDevice> failedRoute = 
                            new LinkedList<>();
                    failedRoute.add(sender);
                    failedRoute.add(nicForTest);
                    return new NetworkModelTestResult(false, routes, failedRoute);
                }
            }
        }

        return new NetworkModelTestResult(true, routes, null);
    }

    public boolean SendData(IpAddress sourceIP, Object data, IpAddress target)
    {
        NIC sourceDev = FindNICByIP(sourceIP);
        NIC targetDev = FindNICByIP(target);
        if(sourceDev != null && targetDev != null)
        {
            sourceDev.sendData(data, target);
            if (data.equals(targetDev.GetIncomingData()))
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

    public boolean ChangeDeviceIP(IpBasedNetworkDevice dev, IpAddress newIP)
    {
        if(networkDevices.containsKey(dev.getMacAddress()))
        {
            ((IpBasedNetworkDevice)networkDevices.get(dev.getMacAddress())).setIpAderss(newIP);
            return true;
        }

        return false;
    }
    
    public boolean ChangeNICGateway(NIC dev, IpAddress newGatewayIP)
    {
        if(networkDevices.containsKey(dev.getMacAddress()))
        {
            ((NIC)networkDevices.get(dev.getMacAddress())).setGateway(newGatewayIP);
            return true;
        }
        
        return false;
    }

    public NetworkDevice FindByMac(MacAddress address)
    {
        return networkDevices.get(address);
    }
    
    public IpAddress getNextInputInSubnet()
    {
        return IpAddress.getNextAddress(lastAddedAddress);
    }

    private NIC FindNICByIP(IpAddress adress)
    {
        for(NetworkDevice dev : networkDevices.values())
        {
            if(dev.getType() == NetworkDeviceType.NIC)
                if(((NIC)dev).getIpAddress().equals(adress))
                    return (NIC)dev;
        }

        return null;
    }

    private IpAddress lastAddedAddress;
    private final HashMap<MacAddress, NetworkDevice> networkDevices;
}