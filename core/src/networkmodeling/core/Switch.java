package networkmodeling.core;

import java.util.HashMap;

public class Switch extends NetworkDevice {

    public Switch(MacAddress macAddress, int portCount) {
        super(macAddress, portCount);

        camTable = new HashMap<>();
        deviceType = NetworkDeviceType.Switch;
    }

    public Switch() {
        this(MacAddress.getRandomAddress(), 8);
        deviceType = NetworkDeviceType.Switch;
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
        camTable.put(frame.getSenderMac(), receivingPort);

        frame.getRoute().add(this);

        Port outputPort = camTable.get(frame.getTargetMac());
        if (outputPort == null) {
            broadcastFrame(frame, receivingPort);
        } else {
            try {
                outputPort.sendFrame(frame);
            } catch (Exception ex) {
                System.err.println("Attempt send through unbound switch port");
            }
        }
    }

    @Override
    public String getDescription() {
        return String.format("Switch (%s)", getMacAddress().toString());
    }

    private void broadcastFrame(Frame frame, Port receivingPort) {
        for (Port port : getPorts()) {
            if (port != receivingPort) {
                try {
                    //port.sendFrame(frame);
                    port.sendFrame(new Frame(frame));
                } catch (Exception ex) {
                    System.err.println(
                        "Attempt send through unbound switch port");
                }
            }
        }
    }


    private final HashMap<MacAddress, Port> camTable;
}
