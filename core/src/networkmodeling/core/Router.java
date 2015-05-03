package networkmodeling.core;

public class Router extends IpBasedNetworkDevice {

    public Router(MacAddress macAddress, IpAddress ipAddress, int portCount) {
        super(NetworkDeviceType.Router, macAddress, ipAddress, portCount);
    }

    @Override
    public void handleIncomingFrame(Frame frame, Port receivingPort) {
    }

    @Override
    public String getDescription() {
        return String.format("Router @ %s (%s)", getIpAddress().toString(),
            getMacAddress().toString());
    }

}
