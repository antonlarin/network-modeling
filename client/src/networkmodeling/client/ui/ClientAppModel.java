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
import networkmodeling.core.NetworkDevice;
import networkmodeling.core.NetworkModelTestResult;
import networkmodeling.core.NetworkVisualModel;
import networkmodeling.core.Router;
import networkmodeling.core.RoutingTable;
import networkmodeling.core.Switch;
import networkmodeling.core.modelgraph.NetworkGraphEdge;
import networkmodeling.core.modelgraph.NetworkGraphNode;
import networkmodeling.exceptions.NoFreePortsException;

public class ClientAppModel {

    public ClientAppModel() {
        visualModel = new NetworkVisualModel();
        clientDaemon = new ClientDaemon(this);
        pcs = new PropertyChangeSupport(this);
        selectedNode = null;
        selectedEdge = null;
        networkTestRunner = null;
        networkTestResult = null;
        stashedRoutingTable = null;
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

    public void setVisualModelChanged() {
        pcs.firePropertyChange("visualModel", null, visualModel);
    }

    public void addNICWithProperties(String ip, String gateway,
        Point2D.Double location) {
        NIC nic = new NIC(MacAddress.getRandomAddress(), new IpAddress(ip),
            new IpAddress(gateway));
        addDevice(nic, location);
    }

    public void addHubWithProperties(String portsCount,
        Point2D.Double location) {
        Hub hub = new Hub(MacAddress.getRandomAddress(),
            Integer.valueOf(portsCount));
        addDevice(hub, location);
    }

    public void addSwitchWithProperties(String portsCount,
        Point2D.Double location) {
        Switch networkSwitch = new Switch(MacAddress.getRandomAddress(),
            Integer.valueOf(portsCount));
        addDevice(networkSwitch, location);
    }

    public void addRouterWithProperties(String ip, String portsCount,
        Point2D.Double location) {
        Router router = new Router(MacAddress.getRandomAddress(),
            new IpAddress(ip), Integer.valueOf(portsCount));
        if (stashedRoutingTable != null) {
            router.setRoutingTable(stashedRoutingTable);
        }
        addDevice(router, location);
    }

    public void connectDevices(NetworkGraphNode dev1, NetworkGraphNode dev2)
        throws NoFreePortsException {
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

    public void setGatewayForSelectedDevice(String gatewayString) {
        IpAddress newGateway = new IpAddress(gatewayString);
        clientDaemon.SendChangeNICGatewayRequest(selectedNode, newGateway);
        visualModel.ChangeNICGateway(selectedNode, newGateway);
    }

    public void setStashedRoutingTableForSelectedRouter() {

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

    public void setStashedRoutingTable(RoutingTable routingTable) {
        RoutingTable oldStashedRoutingTable = stashedRoutingTable;
        stashedRoutingTable = routingTable;
        pcs.firePropertyChange("stashedRoutingTable", oldStashedRoutingTable,
            stashedRoutingTable);
    }

    public RoutingTable getStashedRoutingTable() {
        RoutingTable result = stashedRoutingTable;
        stashedRoutingTable = null;
        return result;
    }



    private void addDevice(NetworkDevice device, Point2D.Double location) {
        NetworkGraphNode deviceNode = new NetworkGraphNode(device,
            location.x, location.y);
        clientDaemon.SendAddDevicesRequest(deviceNode);
        visualModel.AddDevice(deviceNode);
        pcs.firePropertyChange("visualModel", null, visualModel);
    }



    private final PropertyChangeSupport pcs;
    private ClientDaemon clientDaemon;
    private NetworkVisualModel visualModel;
    private NetworkGraphNode selectedNode;
    private NetworkGraphEdge selectedEdge;
    private NetworkTestRunner networkTestRunner;
    private NetworkModelTestResult networkTestResult;
    private RoutingTable stashedRoutingTable;



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
