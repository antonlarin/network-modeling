package networkmodeling.core;

import java.util.HashMap;
import java.util.LinkedList;

public class NIC extends NetworkDevice {
    public NIC(MacAddress macAddress, IpAddress ipAddress) {
        super(macAddress, 1);
        this.ipAddress = ipAddress;
        this.ipMacTable = new HashMap<>();
        this.incomingData = new LinkedList<>();
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
            updateIpMacTable(arpRequest);
            if (!arpRequest.isResponse() &&
                arpRequest.getTargetIp().equals(ipAddress)) {
                ARPRequest arpResponse = new ARPRequest(ipAddress,
                    getMacAddress(), arpRequest.getSenderIp(), true);
                Frame arpResponseFrame = new Frame(arpRequest.getSenderMac(),
                    arpResponse);
                try {
                    getPort().sendFrame(arpResponseFrame);
                } catch (Exception ex) {
                    // Swallow exception
                }
            }
            break;
        default:
            throw new IllegalArgumentException("Illegal frame data type");
        }
    }


    private void updateIpMacTable(ARPRequest response) {
        ipMacTable.put(response.getSenderIp(), response.getSenderMac());
    }

    private Port getPort() {
        return getPorts()[0];
    }


    private final IpAddress ipAddress;
    private final LinkedList<Object> incomingData;
    private final HashMap<IpAddress, MacAddress> ipMacTable;
}
