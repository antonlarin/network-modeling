package networkmodeling.core;

public class RoutingTableRecord {

    public RoutingTableRecord(SubnetIpDescription subnetAddress,
        IpAddress hopIp, int portIndex) {
        this.subnetAddress = subnetAddress;
        this.hopIp = hopIp;
        this.portIndex = portIndex;
    }

    public SubnetIpDescription getSubnetAddress() {
        return subnetAddress;
    }

    public IpAddress getHopIp() {
        return hopIp;
    }

    public int getPortIndex() {
        return portIndex;
    }

    public boolean correspondsToAddress(IpAddress ipAddress) {
        return subnetAddress.containsAddress(ipAddress);
    }



    private final SubnetIpDescription subnetAddress;
    private final IpAddress hopIp;
    private final int portIndex;
}
