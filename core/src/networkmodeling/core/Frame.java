package networkmodeling.core;

public class Frame {
    public Frame(MacAddress target, FrameData data) {
        this.target = target;
        this.data = data;
    }
    
    public MacAddress getTargetMac() {
        return target;
    }
    
    public FrameData getData() {
        return data;
    }
    
    private final MacAddress target;
    private final FrameData data;
}
