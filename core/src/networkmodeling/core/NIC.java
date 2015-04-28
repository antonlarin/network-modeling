package networkmodeling.core;

import java.util.LinkedList;


public class NIC extends IpBasedNetworkDevice {

    public NIC(MacAddress macAddress, IpAddress ipAddress,
        IpAddress gatewayIp) {
        super(macAddress, ipAddress, 1);
        this.incomingData = new LinkedList<>();
        this.gatewayIp = gatewayIp;
    }

    public NIC() {
        this(MacAddress.getRandomAddress(),
            new IpAddress((short)0, (short)0, (short)0, (short)0),
            new IpAddress((short)0, (short)0, (short)0, (short)0));
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
            processArpRequest(frame, receivingPort);
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



    private Port getPort() {
        return getPorts()[0];
    }

    private boolean sendDataInsideSubnet(Object data, IpAddress targetIp) {
        MacAddress targetMac = resolveMacAddress(targetIp);

        // If target MAC is unknown try to resolve it with ARP request
        if (targetMac == null) {
            sendArpRequest(targetIp, getPort());
            targetMac = resolveMacAddress(targetIp);

            // If can't resolve MAC even after ARP, then IP is not on subnet
            if (targetMac == null) {
                return false;
            }
        }

        Packet packet = new Packet(getIpAddress(), targetIp, data);
        Frame frame = new Frame(getMacAddress(), targetMac, packet);
        try {
            getPort().sendFrame(frame);
        } catch (Exception ex) {
            System.err.println("Attempt send through unbound NIC port");
        }

        return true;
    }

    private void sendDataThroughGateway(Object data, IpAddress target) {
        MacAddress gatewayMac = resolveMacAddress(gatewayIp);

        // If gateway's MAC is unknown, try to resolve it with ARP request
        if (gatewayMac == null) {
            sendArpRequest(gatewayIp, getPort());
            gatewayMac = resolveMacAddress(gatewayIp);

            // If gateway's MAC wasn't resolved, give up
            if (gatewayMac == null) {
                return;
            }
        }

        Packet packet = new Packet(getIpAddress(), target, data);
        Frame frame = new Frame(getMacAddress(), gatewayMac, packet);
        try {
            getPort().sendFrame(frame);
        } catch (Exception ex) {
            System.err.println("Attempt send through unbound NIC port");
        }
    }
    
    public Object GetIncomingData()
    {
        return incomingData.pop();
    }
    
    public boolean isData()
    {
        return incomingData.isEmpty();
    }
            

    private final IpAddress gatewayIp;
    private final LinkedList<Object> incomingData;
}
