package networkmodeling.core;

public class RoutingTableRecord {

    public RoutingTableRecord(SubnetIpDescription subnetIpDescription,
        IpAddress hopIp, int portIndex) {
        this.subnetIpDescription = subnetIpDescription;
        this.hopIp = hopIp;
        this.portIndex = portIndex;
    }

    public IpAddress getHopIp() {
        return hopIp;
    }

    public int getPortIndex() {
        return portIndex;
    }

    public boolean correspondsToAddress(IpAddress ipAddress) {
        return subnetIpDescription.containsAddress(ipAddress);
    }



    private final SubnetIpDescription subnetIpDescription;
    private final IpAddress hopIp;
    private final int portIndex;
}
