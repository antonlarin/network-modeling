package networkmodeling.core;

import java.io.Serializable;
import java.util.Objects;
import networkmodeling.exceptions.NoFreePortsException;

public abstract class NetworkDevice implements Serializable {

    public NetworkDevice(NetworkDeviceType deviceType, MacAddress macAddress, int portCount) {
        this.macAddress = macAddress;
        this.ports = new Port[portCount];
        for (int i = 0; i < portCount; ++i) {
            this.ports[i] = new Port(this);
        }
        this.deviceType = deviceType;
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

        if (localPort != null) {
            if (remotePort != null) {
                localPort.bind(remotePort);
            } else {
                throw new NoFreePortsException(other);
            }
        } else {
            throw new NoFreePortsException(this);
        }
    }

    public boolean isConnectedTo(NetworkDevice otherDevice) {
        for (Port port : ports) {
            Port otherPort = port.GetConnectedPort();
            if (otherPort.isBound() && otherPort.getDevice().equals(otherDevice)) {
                return true;
            }
        }

        return false;
    }

    public void DisconnectFrom(NetworkDevice otherDevice)
    {
        for (Port port : ports) {
            Port otherPort = port.GetConnectedPort();
            if (port.isBound() && otherPort.getDevice().equals(otherDevice)) {
                otherPort.unbind();
                port.unbind();
                return;
            }
        }
    }

    public void DisconnectFromAll()
    {
        for (Port port : ports) {
            Port otherPort = port.GetConnectedPort();
            if (port.isBound()) {
                port.unbind();
                otherPort.unbind();
            }
        }
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public int getPortsCount() {
        return ports.length;
    }

    public NetworkDeviceType getType()
    {
        return deviceType;
    }

    public abstract String getDescription();

    public int getIndexOfPortConnectedTo(NetworkDevice device) {
        for (int i = 0; i < ports.length; ++i) {
            if (ports[i].isBound() &&
                ports[i].GetConnectedPort().getDevice().equals(device)) {
                return i;
            }
        }

        return -1;
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
    private final NetworkDeviceType deviceType;
}
