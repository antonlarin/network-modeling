package networkmodeling.core;

import java.util.LinkedList;

public class Frame {
    public Frame(MacAddress sender, MacAddress target, FrameData data) {
        this.sender = sender;
        this.target = target;
        this.data = data;
        this.route = new LinkedList<>();
    }
    public Frame(Frame source)
    {
        this.sender = source.sender;
        this.target = source.target;
        this.data = source.data;
        this.route = new LinkedList<>(source.getRoute());
    }

    public MacAddress getSenderMac() {
        return sender;
    }

    public MacAddress getTargetMac() {
        return target;
    }

    public FrameData getData() {
        return data;
    }
    
    public LinkedList<NetworkDevice> getRoute()
    {
        return route;
    }

    private final MacAddress sender;
    private final MacAddress target;
    private final FrameData data;
    private final LinkedList<NetworkDevice> route;
}
