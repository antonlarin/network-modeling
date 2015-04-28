package networkmodeling.core;

public class Hub extends NetworkDevice {
    public Hub(MacAddress macAddress, int portCount) {
        super(macAddress, portCount);
    }

    public Hub() {
        this(MacAddress.getRandomAddress(), 8);
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
        for (Port port : getPorts()) {
            if (port != receivingPort) {
                try {
                    port.sendFrame(frame);
                } catch (UnboundPortException ex) {
                    System.err.println("Hub's port unbound.");
                }
            }
        }
    }
}
