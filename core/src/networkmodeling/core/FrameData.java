package networkmodeling.core;

public abstract class FrameData {
    public FrameData(FrameDataType type) {
        this.type = type;
    }
    
    private final FrameDataType type;
}
