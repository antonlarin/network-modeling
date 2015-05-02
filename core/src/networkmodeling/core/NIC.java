package networkmodeling.core;

import java.util.LinkedList;


public class NIC extends IpBasedNetworkDevice {

    public NIC(MacAddress macAddress, IpAddress ipAddress,
        IpAddress gatewayIp) {
        super(macAddress, ipAddress, 1);
        this.incomingData = new LinkedList<>();
        this.gatewayIp = gatewayIp;
        this.lastIncomingDataRoute = new LinkedList<>();
        deviceType = NetworkDeviceType.NIC;
    }

    public NIC() {
        this(MacAddress.getRandomAddress(),
            new IpAddress((short)0, (short)0, (short)0, (short)0),
            new IpAddress((short)0, (short)0, (short)0, (short)0));
        deviceType = NetworkDeviceType.NIC;
    }

    public IpAddress getGateway() {
        return gatewayIp;
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
        FrameData frameData = frame.getData();
        switch (frameData.getType()) {
        case PACKET:
            Packet incomingPacket = (Packet) frameData;

            if(incomingPacket.getTargetIp() == this.getIpAddress())
                frame.getRoute().add(this);
            lastIncomingDataRoute = frame.getRoute();

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

    @Override
    public String getDescription() {
        return String.format("NIC @ %s (%s)", getIpAddress().toString(),
            getMacAddress().toString());
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
            frame.getRoute().add(this);
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
            frame.getRoute().add(this);
            getPort().sendFrame(frame);
        } catch (Exception ex) {
            System.err.println("Attempt send through unbound NIC port");
        }
    }

    public Object GetIncomingData()
    {
        if(incomingData.isEmpty())
            return new String();
        return incomingData.pop();
    }

    public boolean isData()
    {
        return incomingData.isEmpty();
    }

    public LinkedList<NetworkDevice> getLastIncomingDataRoute()
    {
        return lastIncomingDataRoute;
    }

    private LinkedList<NetworkDevice> lastIncomingDataRoute;
    private final IpAddress gatewayIp;
    private final LinkedList<Object> incomingData;
}
