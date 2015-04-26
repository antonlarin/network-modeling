package networkmodeling.core;

public class Port {
    public Port(NetworkDevice device) {
        this.device = device;
        this.other = null;
    }

    public boolean bind(Port other) {
        if (!isBound() && !other.isBound()) {
            this.other = other;
            other.other = this;
            return true;
        } else {
            return false;
        }
    }

    public void unbind() {
        other = null;
    }

    public boolean isBound() {
        return other != null;
    }

    public void sendFrame(Frame frame) throws UnboundPortException {
        if (!isBound()) {
            throw new UnboundPortException();
        }

        other.receiveFrame(frame);
    }

    public void receiveFrame(Frame frame) {
        device.handleIncomingFrame(frame, this);
    }

    private final NetworkDevice device;
    private Port other;
}
