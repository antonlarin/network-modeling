package networkmodeling.core;

import networkmodeling.exceptions.UnboundPortException;

public class Hub extends NetworkDevice {
    public Hub(MacAddress macAddress, int portCount) {
        super(NetworkDeviceType.Hub, macAddress, portCount);
    }

    public Hub() {
        this(MacAddress.getRandomAddress(), 8);
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
        frame.getRoute().add(this);
        for (Port port : getPorts()) {
            if (port != receivingPort) {
                try {
                    port.sendFrame(new Frame(frame));
                } catch (UnboundPortException ex) {
                    // ignore empty ports
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return String.format("Hub (%s)", getMacAddress().toString());
    }
}
