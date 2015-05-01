package networkmodeling.core;

import java.io.Serializable;
import networkmodeling.exceptions.UnboundPortException;

public class Port implements Serializable {
    public Port(NetworkDevice device) {
        this.device = device;
        this.other = null;
    }

    public NetworkDevice getDevice() {
        return device;
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

    public Port GetConnectedPort() {
        return other;
    }

    private final NetworkDevice device;
    private Port other;
}
