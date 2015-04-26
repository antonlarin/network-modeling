package networkmodeling.core;

import java.util.HashMap;
import java.util.LinkedList;


public class NIC extends NetworkDevice {
    public NIC(MacAddress macAddress, IpAddress ipAddress,
        IpAddress gatewayIp) {
        super(macAddress, 1);
        this.ipAddress = ipAddress;
        this.ipMacTable = new HashMap<>();
        this.incomingData = new LinkedList<>();
        this.gatewayIp = gatewayIp;
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
        FrameData frameData = frame.getData();
        switch (frameData.getType()) {
        case PACKET:
            Packet incomingPacket = (Packet) frameData;
            incomingData.add(incomingPacket.getData());
            break;
        case ARP_REQUEST:
            ARPRequest arpRequest = (ARPRequest) frameData;
            updateIpMacTable(frame.getSenderMac(), arpRequest);
            if (arpRequest.isResponse()) {
                ipMacTable.put(arpRequest.getSenderIp(), frame.getSenderMac());
            } else {
                if (arpRequest.getTargetIp().equals(ipAddress)) {
                    ARPRequest arpResponse = new ARPRequest(ipAddress,
                        arpRequest.getSenderIp(), true);
                    Frame arpResponseFrame = new Frame(getMacAddress(),
                        frame.getSenderMac(), arpResponse);
                    try {
                        getPort().sendFrame(arpResponseFrame);
                    } catch (UnboundPortException ex) {
                        System.err.println(
                            "Send attempt through unbound NIC port");
                    }
                }
            }
            break;
        default:
            throw new IllegalArgumentException("Illegal frame data type");
        }
    }

    public void sendData(Object data, IpAddress target) {
        boolean sendInsideSubnetWasSuccessful =
            sendDataInsideSubnet(data, target);

        if (!sendInsideSubnetWasSuccessful) {
            sendDataThroughGateway(data, target);
        }
    }


    private void updateIpMacTable(MacAddress senderMac, ARPRequest response) {
        ipMacTable.put(response.getSenderIp(), senderMac);
    }

    private Port getPort() {
        return getPorts()[0];
    }

    private boolean sendDataInsideSubnet(Object data, IpAddress targetIp) {
        MacAddress targetMac = ipMacTable.get(targetIp);

        // If target MAC is unknown try to resolve it with ARP request
        if (targetMac == null) {
            ARPRequest arpRequest = new ARPRequest(ipAddress, targetIp, false);
            Frame arpRequestFrame = new Frame(getMacAddress(),
                MacAddress.getBroadcastAddress(), arpRequest);
            try {
                getPort().sendFrame(arpRequestFrame);
            } catch (UnboundPortException ex) {
                System.err.println("Attempt send through unbound NIC port");
            }
            targetMac = ipMacTable.get(targetIp);

            // If can't resolve MAC even after ARP, then IP is not on subnet
            if (targetMac == null) {
                return false;
            }
        }

        Packet packet = new Packet(ipAddress, targetIp, data);
        Frame frame = new Frame(getMacAddress(), targetMac, packet);
        try {
            getPort().sendFrame(frame);
        } catch (Exception ex) {
            System.err.println("Attempt send through unbound NIC port");
        }

        return true;
    }

    private void sendDataThroughGateway(Object data, IpAddress target) {
        MacAddress gatewayMac = ipMacTable.get(gatewayIp);

        // If gateway's MAC is unknown, try to resolve it with ARP request
        if (gatewayMac == null) {
            ARPRequest arpRequest = new ARPRequest(ipAddress, gatewayIp, false);
            Frame arpRequestFrame = new Frame(getMacAddress(),
                MacAddress.getBroadcastAddress(), arpRequest);
            try {
                getPort().sendFrame(arpRequestFrame);
            } catch (Exception ex) {
                System.err.println("Attempt send through unbound NIC port");
            }

            gatewayMac = ipMacTable.get(gatewayIp);

            // If gateway's MAC wasn't resolved, give up
            if (gatewayMac == null) {
                return;
            }
        }

        Packet packet = new Packet(ipAddress, target, data);
        Frame frame = new Frame(getMacAddress(), gatewayMac, packet);
        try {
            getPort().sendFrame(frame);
        } catch (Exception ex) {
            System.err.println("Attempt send through unbound NIC port");
        }
    }


    private final IpAddress ipAddress;
    private final IpAddress gatewayIp;
    private final LinkedList<Object> incomingData;
    private final HashMap<IpAddress, MacAddress> ipMacTable;
}
