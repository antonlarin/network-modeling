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
                    frame.getRoute().add(this);
                    try {
                        getPorts()[routeRecord.getPortIndex()].sendFrame(frame);
                    } catch (UnboundPortException ex) {
                        Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    MacAddress hopMac = resolveMacAddress(
                        routeRecord.getHopIp());

                    if (hopMac == null) {
                        ARPRequest arpRequest = new ARPRequest(getIpAddress(),
                        routeRecord.getHopIp(), false);

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


    private final RoutingTable routingTable;
}
