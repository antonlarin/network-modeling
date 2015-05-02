package networkmodeling.client.ui;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import networkmodeling.client.ClientDaemon;
import networkmodeling.core.Hub;
import networkmodeling.core.IpAddress;
import networkmodeling.core.MacAddress;
import networkmodeling.core.NIC;
import networkmodeling.core.NetworkModelTestResult;
import networkmodeling.core.NetworkVisualModel;
import networkmodeling.core.Switch;
import networkmodeling.core.modelgraph.NetworkGraphEdge;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class ClientAppModel {

    public ClientAppModel() {
        visualModel = new NetworkVisualModel();
        clientDaemon = new ClientDaemon(this);
        pcs = new PropertyChangeSupport(this);
        selectedNode = null;
        selectedEdge = null;
        networkTestRunner = null;
        networkTestResult = null;
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
        pcs.firePropertyChange("selectedNode", oldSelectedDevice, device);
    }

    public NetworkGraphEdge getSelectedEdge() {
        return selectedEdge;
    }

    public void setSelectedEdge(NetworkGraphEdge connection) {
        NetworkGraphEdge oldSelectedEdge = selectedEdge;
        selectedEdge = connection;
        pcs.firePropertyChange("selectedEdge", oldSelectedEdge, connection);
    }

    public NetworkModelTestResult getNetworkTestResult() {
        return networkTestResult;
    }

    public void setNetworkTestResult(NetworkModelTestResult result) {
        networkTestResult = result;
        pcs.firePropertyChange("networkTestResult", null, result);
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

    public void addHubWithProperties(String portsCount,
        Point2D.Double location) {
        Hub hub = new Hub(MacAddress.getRandomAddress(),
            Integer.valueOf(portsCount));
        NetworkGraphNode deviceNode = new NetworkGraphNode(hub,
            location.x, location.y);
        clientDaemon.SendAddDevicesRequest(deviceNode);
        visualModel.AddDevice(deviceNode);
        pcs.firePropertyChange("visualModel", null, visualModel);
    }

    public void addSwitchWithProperties(String portsCount,
        Point2D.Double location) {
        Switch networkSwitch = new Switch(MacAddress.getRandomAddress(),
            Integer.valueOf(portsCount));
        NetworkGraphNode deviceNode = new NetworkGraphNode(networkSwitch,
            location.x, location.y);
        clientDaemon.SendAddDevicesRequest(deviceNode);
        visualModel.AddDevice(deviceNode);
        pcs.firePropertyChange("visualModel", null, visualModel);
    }

    public void connectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2) {
        clientDaemon.SendConnectDevicesRequest(dev1, dev2);
        visualModel.ConnectDevices(dev1, dev2);
    }

    public void removeSelectedEdge() {
        NetworkGraphNode end1 = selectedEdge.getFirstNode();
        NetworkGraphNode end2 = selectedEdge.getSecondNode();
        clientDaemon.SendDisconnectDevicesRequest(end1, end2);
        visualModel.DisconnectDevices(end1, end2);
        setSelectedEdge(null);
        pcs.firePropertyChange("visualModel", null, visualModel);
    }

    public void removeSelectedNode() {
        clientDaemon.SendDeleteDeviceRequest(selectedNode);
        visualModel.DeleteDevice(selectedNode);
        setSelectedNode(null);
        pcs.firePropertyChange("visualModel", null, visualModel);
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

    public void testNetwork() {
        networkTestResult = null;
        networkTestRunner = new NetworkTestRunner();
        networkTestRunner.execute();
    }


    private final PropertyChangeSupport pcs;
    private ClientDaemon clientDaemon;
    private NetworkVisualModel visualModel;
    private NetworkGraphNode selectedNode;
    private NetworkGraphEdge selectedEdge;
    private NetworkTestRunner networkTestRunner;
    private NetworkModelTestResult networkTestResult;



    private class NetworkTestRunner
        extends SwingWorker<NetworkModelTestResult, Void> {

        @Override
        protected NetworkModelTestResult doInBackground() {
            return visualModel.GetModel().TestNetwork();
        }

        @Override
        protected void done() {
            try {
                setNetworkTestResult(get());
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ClientAppModel.class.getName()).log(
                    Level.SEVERE, null, ex);
            }
        }
    }
}
