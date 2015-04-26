package networkmodeling.core;

public class Frame {
    public Frame(MacAddress sender, MacAddress target, FrameData data) {
        this.sender = sender;
        this.target = target;
        this.data = data;
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

    private final MacAddress sender;
    private final MacAddress target;
    private final FrameData data;
}
