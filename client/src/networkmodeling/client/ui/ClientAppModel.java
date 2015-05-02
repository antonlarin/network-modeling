package networkmodeling.client.ui;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import networkmodeling.client.ClientDaemon;
import networkmodeling.core.IpAddress;
import networkmodeling.core.MacAddress;
import networkmodeling.core.NIC;
import networkmodeling.core.NetworkVisualModel;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class ClientAppModel {
    
    public ClientAppModel() {
        visualModel = new NetworkVisualModel();
        clientDaemon = new ClientDaemon(this);
        pcs = new PropertyChangeSupport(this);
        selectedNode = null;
    }
    
    public ClientDaemon getClientDaemon() {
        return clientDaemon;
    }
    
    public void resetClientDaemon() {
        clientDaemon = new ClientDaemon(this);
    }
    
    public NetworkVisualModel getVisualModel() {
        return visualModel;
    }
    
    public void setVisualModel(NetworkVisualModel visualModel) {
        this.visualModel = visualModel;
    }

    public NetworkGraphNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(NetworkGraphNode device) {
        NetworkGraphNode oldSelectedDevice = selectedNode;
        selectedNode = device;
        pcs.firePropertyChange("selectedNode",
            oldSelectedDevice, device);
    }

    public void addPropertyChangeListener(String propertyName,
        PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName,
        PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
    
    public void addNICWithProperties(String ip, String gateway,
        Point2D.Double location) {
        NIC nic = new NIC(MacAddress.getRandomAddress(), new IpAddress(ip),
            new IpAddress(gateway));
        NetworkGraphNode deviceNode = new NetworkGraphNode(nic,
            location.x, location.y);
        clientDaemon.SendAddDevicesRequest(deviceNode);
        visualModel.AddDevice(deviceNode);
        pcs.firePropertyChange("visualModel", null, visualModel);
    }
    
    public void connectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2) {
        clientDaemon.SendConnectDevicesRequest(dev1, dev2);
        visualModel.ConnectDevices(dev1, dev2);
    }
    
    public void setIpForSelectedDevice(String ipString) {
        IpAddress newAddress = new IpAddress(ipString);
        clientDaemon.SendChangeDeviceIPRequest(selectedNode, newAddress);
        visualModel.ChangeDeviceIP(selectedNode, newAddress);
    }
    
    public void changeSelectedNodeLocation(Point2D.Double newLocation) {
        clientDaemon.SendСhangeNodeCoordinatesRequest(selectedNode,
            newLocation.x, newLocation.y);
        selectedNode.setX(newLocation.getX());
        selectedNode.setY(newLocation.getY());
    }
    
    
    private final PropertyChangeSupport pcs;
    private ClientDaemon clientDaemon;
    private NetworkVisualModel visualModel;
    private NetworkGraphNode selectedNode;
}
