package networkmodeling.core.modelgraph;

import java.io.Serializable;
import networkmodeling.core.NetworkDevice;

public class NetworkGraphNode implements Serializable {
    
    public NetworkGraphNode(NetworkDevice dev, double _x, double _y)
    {
        nodeDevice = dev;
        x = _x;
        y = _y;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(! (o instanceof NetworkGraphNode))
            return false;
        
        NetworkGraphNode otherNode =
                (NetworkGraphNode)o;
        
        return otherNode.getNodeDevice().
                getMacAddress().equals(nodeDevice.getMacAddress());
    }
    
    public void setNodeDevice(NetworkDevice nodeDevice) {
        this.nodeDevice = nodeDevice;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public NetworkDevice getNodeDevice() {
        return nodeDevice;
    }
    
    private NetworkDevice nodeDevice;
    private double x;
    private double y;

}