package networkmodeling.core;

import java.util.LinkedList;

public class Frame {

    public Frame(MacAddress sender, MacAddress target, FrameData data,
        LinkedList<NetworkDevice> route) {
        this.sender = sender;
        this.target = target;
        this.data = data;
        this.route = new LinkedList<>(route);
    }

    public Frame(MacAddress sender, MacAddress target, FrameData data) {
        this(sender, target, data, new LinkedList<>());
    }

    public Frame(Frame source)
    {
        this(source.sender, source.target, source.data, source.getRoute());
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
