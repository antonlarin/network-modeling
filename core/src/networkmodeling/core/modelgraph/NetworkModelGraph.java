package networkmodeling.core.modelgraph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import networkmodeling.core.IpAddress;
import networkmodeling.core.MacAddress;

public class NetworkModelGraph implements Serializable {
    
    public NetworkModelGraph()
    {
        graphNodes = new LinkedList<>();
        graphEdges = new LinkedList<>();
    }
    
    public boolean AddDevice(NetworkGraphNode dev)
    {
        if(!graphNodes.contains(dev))
        {
            graphNodes.add(dev);
            return true;
        }
        return false;
    }
    public boolean ConnectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2)
    {
        NetworkGraphNode localDev1 = FindNodeByMac(dev1.getNodeDevice().getMacAddress());
        NetworkGraphNode localDev2 = FindNodeByMac(dev2.getNodeDevice().getMacAddress());

        NetworkGraphEdge newEdge = new NetworkGraphEdge(localDev1, localDev2);
        
        if(!graphEdges.contains(newEdge))
        {
            graphEdges.add(newEdge);
            return true;
        }
        return false;
    }
    public boolean DisconnectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2)
    {
        NetworkGraphEdge edgeForDel = new NetworkGraphEdge(dev1, dev2);
        if(graphEdges.contains(edgeForDel))
        {
            Iterator<NetworkGraphEdge> i = graphEdges.iterator();
            while(i.hasNext())
            {
                NetworkGraphEdge currentEdge = i.next();
                if(currentEdge.equals(edgeForDel))
                {
                    i.remove();
                    return true;
                }
            }
        }
        return false;
    }
    public boolean DeleteDevice(NetworkGraphNode dev)
    {
        boolean isOperationPerformed = graphNodes.remove(dev);
        
        Iterator<NetworkGraphEdge> i = graphEdges.iterator();
        while(i.hasNext())
        {
            NetworkGraphEdge currentEdge = i.next();
            if(currentEdge.ContainsNode(dev))
                i.remove();
        }
        
        return isOperationPerformed;
    }
    public boolean ChangeDeviceIP(NetworkGraphNode dev, IpAddress newIP)
    {
        return true;
    }
    
    public boolean ChangeNodeCoordinates(NetworkGraphNode dev, double x, double y)
    {
        if(graphNodes.contains(dev))
        {
            Iterator<NetworkGraphNode> i = graphNodes.iterator();
            while(i.hasNext())
            {
                NetworkGraphNode currentNode = i.next();
                if(currentNode.equals(dev))
                {
                      currentNode.setX(x);
                      currentNode.setY(y);
                      return true;
                }
            }
        }
        return false;
    }
    
    public LinkedList<NetworkGraphNode> getNodes() {
        return graphNodes;
    }

    public LinkedList<NetworkGraphEdge> getEdges() {
        return graphEdges;
    }
    
    private NetworkGraphNode FindNodeByMac(MacAddress mac)
    {
        Iterator<NetworkGraphNode> i = graphNodes.iterator();
            while(i.hasNext())
            {
                NetworkGraphNode currentNode = i.next();
                if(currentNode.getNodeDevice().getMacAddress().equals(mac))
                    return currentNode;
            }
        return null;
    }
    
    private LinkedList<NetworkGraphNode> graphNodes;
    private LinkedList<NetworkGraphEdge> graphEdges;
}