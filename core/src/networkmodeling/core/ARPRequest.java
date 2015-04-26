package networkmodeling.core;

public class ARPRequest extends FrameData {
    public ARPRequest(IpAddress senderIp, IpAddress targetIp,
        boolean isResponse) {
        super(FrameDataType.ARP_REQUEST);

        this.senderIp = senderIp;
        this.targetIp = targetIp;
        this.isResponse = isResponse;
    }

    public IpAddress getSenderIp() {
        return senderIp;
    }


    public IpAddress getTargetIp() {
        return targetIp;
    }

    public boolean isResponse() {
        return isResponse;
    }


    private final IpAddress senderIp;
    private final IpAddress targetIp;
    private final boolean isResponse;
}
