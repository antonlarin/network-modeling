package networkmodeling.core;

public abstract class FrameData {
    public FrameData(FrameDataType type) {
        this.type = type;
    }

    public FrameDataType getType() {
        return type;
    }

    private final FrameDataType type;
}
