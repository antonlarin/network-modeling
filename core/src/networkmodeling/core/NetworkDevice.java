package networkmodeling.core;

public abstract class NetworkDevice {

    public NetworkDevice(MacAddress macAddress, int portCount) {
        this.macAddress = macAddress;
        this.ports = new Port[portCount];
    }

    public abstract void handleIncomingFrame(Frame frame, Port receivingPort);

    public void connectTo(NetworkDevice other) throws NoFreePortsException {
        Port localPort = this.getFreePort();
        Port remotePort = other.getFreePort();

        if (localPort != null && remotePort != null) {
            localPort.bind(remotePort);
        } else {
            throw new NoFreePortsException();
        }
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    protected Port getFreePort() {
        for (Port port : ports) {
            if (!port.isBound()) {
                return port;
            }
        }

        return null;
    }

    protected Port[] getPorts() {
        return ports;
    }

    private final MacAddress macAddress;
    private final Port[] ports;
}
