package networkmodeling.core;

import java.util.Objects;

public abstract class NetworkDevice {

    public NetworkDevice(MacAddress macAddress, int portCount) {
        this.macAddress = macAddress;
        this.ports = new Port[portCount];
    }

    public abstract void handleIncomingFrame(Frame frame, Port receivingPort);

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NetworkDevice)) {
            return false;
        }

        NetworkDevice otherDevice = (NetworkDevice) o;
        return otherDevice.getMacAddress().equals(macAddress);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.macAddress);
        return hash;
    }

    public void connectTo(NetworkDevice other) throws NoFreePortsException {
        Port localPort = this.getFreePort();
        Port remotePort = other.getFreePort();

        if (localPort != null && remotePort != null) {
            localPort.bind(remotePort);
        } else {
            throw new NoFreePortsException();
        }
    }

    public boolean isConnectedTo(NetworkDevice otherDevice) {
        for (Port port : ports) {
            Port otherPort = port.GetConnectedPort();
            if (otherPort.getDevice().equals(otherDevice)) {
                return true;
            }
        }

        return false;
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
