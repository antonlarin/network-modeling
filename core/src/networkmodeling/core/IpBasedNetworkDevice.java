package networkmodeling.core;

import java.util.HashMap;

public abstract class IpBasedNetworkDevice extends NetworkDevice {

    public IpBasedNetworkDevice(MacAddress macAddress, IpAddress ipAddress,
        int portCount) {
        super(macAddress, portCount);

        this.ipAddress = ipAddress;
        this.ipMacTable = new HashMap<>();
    }

    public void processArpRequest(Frame frame, Port receivingPort) {
        FrameData frameData = frame.getData();
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
                    receivingPort.sendFrame(arpResponseFrame);
                } catch (UnboundPortException ex) {
                    System.err.println(
                        "Send attempt through unbound NIC port");
                }
            }
        }
    }

    public void sendArpRequest(IpAddress targetIp, Port port) {
        ARPRequest arpRequest = new ARPRequest(ipAddress, targetIp, false);
        Frame arpRequestFrame = new Frame(getMacAddress(),
            MacAddress.getBroadcastAddress(), arpRequest);
        try {
            port.sendFrame(arpRequestFrame);
        } catch (UnboundPortException ex) {
            System.err.println("Attempt send through unbound NIC port");
        }
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public MacAddress resolveMacAddress(IpAddress ipAddress) {
        return ipMacTable.get(ipAddress);
    }


    private void updateIpMacTable(MacAddress senderMac, ARPRequest response) {
        ipMacTable.put(response.getSenderIp(), senderMac);
    }


    private final IpAddress ipAddress;
    private final HashMap<IpAddress, MacAddress> ipMacTable;
}
