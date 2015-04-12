package networkmodeling.core;

public class Packet extends FrameData {
    public Packet(IpAddress sender, IpAddress target, Object data) {
        super(FrameDataType.PACKET);
        
        this.sender = sender;
        this.target = target;
        this.data = data;
    }
    
    public IpAddress getSenderIp() {
        return sender;
    }
    
    public IpAddress getTargetIp() {
        return target;
    }
    
    public Object getData() {
        return data;
    }
    
    
    private final IpAddress sender;
    private final IpAddress target;
    private final Object data;
}
