package networkmodeling.core;

public class Hub extends NetworkDevice {
    public Hub(MacAddress macAddress, int portCount) {
        super(macAddress, portCount);
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
        for (Port port : getPorts()) {
            if (port != receivingPort) {
                try {
                    port.sendFrame(frame);
                } catch (Exception ex) {
                    // Swallow exception
                }
            }
        }
    }
}
