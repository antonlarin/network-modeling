package networkmodeling.core;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkmodeling.exceptions.UnboundPortException;

public class Router extends IpBasedNetworkDevice {

    public Router(MacAddress macAddress, IpAddress ipAddress, int portCount) {
        super(NetworkDeviceType.Router, macAddress, ipAddress, portCount);

        routingTable = new RoutingTable();
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
        FrameData frameData = frame.getData();
        switch (frameData.getType()) {
        case PACKET:
            Packet packet = (Packet) frameData;
            RoutingTableRecord routeRecord = routingTable.getRouteForIp(
                packet.getTargetIp());
            if (routeRecord != null) {
                if (routeRecord.getHopIp().equals(IpAddress.getZeroIp())) {
                    LinkedList<NetworkDevice> route = frame.getRoute();
                    route.add(this);
                    Port port = getPorts()[routeRecord.getPortIndex()];
                    MacAddress targetMac = resolveMacAddress(
                            packet.getTargetIp());
                    if (targetMac == null) {
                        sendArpRequest(packet.getTargetIp(), port);
                        
                        targetMac = resolveMacAddress(packet.getTargetIp());
                        if (targetMac == null) {
                            return;
                        }
                    }
                    Frame newFrame = new Frame(getMacAddress(),
                        targetMac, frame.getData(), route);
                    try {
                        port.sendFrame(newFrame);
                    } catch (UnboundPortException ex) {
                        Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    MacAddress hopMac = resolveMacAddress(
                        routeRecord.getHopIp());

                    if (hopMac == null) {
                        sendArpRequest(routeRecord.getHopIp(),
                                getPorts()[routeRecord.getPortIndex()]);

                        hopMac = resolveMacAddress(routeRecord.getHopIp());
                        if (hopMac == null) {
                            // Give up if IP in the routing table is unreachable
                            return;
                        }
                    }

                    LinkedList<NetworkDevice> route = frame.getRoute();
                    route.add(this);
                    Frame newFrame = new Frame(getMacAddress(), hopMac,
                        frameData, route);
                    try {
                        getPorts()[routeRecord.getPortIndex()].sendFrame(
                            newFrame);
                    } catch (UnboundPortException ex) {
                        Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                // Swallow frame
            }
            break;
        case ARP_REQUEST:
            processArpRequest(frame, receivingPort);
            break;
        }
    }

    @Override
    public String getDescription() {
        return String.format("Router @ %s (%s)", getIpAddress().toString(),
            getMacAddress().toString());
    }

    public RoutingTable getRoutingTable()
    {
        return routingTable;
    }

    public void setRoutingTable(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }


    private RoutingTable routingTable;
}
