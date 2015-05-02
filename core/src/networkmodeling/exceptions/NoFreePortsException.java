package networkmodeling.exceptions;

import networkmodeling.core.NetworkDevice;

public class NoFreePortsException extends NMException {

    public NoFreePortsException(NetworkDevice dev) {
        source = dev;
    }

    public String getDescription() {
        return source.getDescription() + " has no free ports.";
    }

    private final NetworkDevice source;
}
