package networkmodeling.core;

public class SubnetIpDescription {

    public SubnetIpDescription(IpAddress subnetIp, byte subnetAddressLength) {
        this.subnetIp = subnetIp;
        this.subnetAddressLength = subnetAddressLength;
    }

    public SubnetIpDescription(String subnetDescription) {
        if (!isValid(subnetDescription)) {
            throw new IllegalArgumentException();
        }

        String[] subnetDescriptionParts = subnetDescription.split("/");
        subnetIp = new IpAddress(subnetDescriptionParts[0]);
        subnetAddressLength = Byte.valueOf(subnetDescriptionParts[1]);
    }

    public boolean containsAddress(IpAddress ip) {
        long ipBits = ip.getBitRepresentation();
        long subnetIpBits = subnetIp.getBitRepresentation();
        return subnetIpBits == (ipBits & (~0 << subnetAddressLength));
    }

    @Override
    public String toString() {
        return String.format("%s/%d", subnetIp.toString(), subnetAddressLength);
    }



    public static boolean isValid(String subnetDescription) {
        String[] subnetDescriptionParts = subnetDescription.split("/");

        byte subnetMaskLength = 0;
        try {
            subnetMaskLength = Byte.valueOf(subnetDescriptionParts[1]);
            if (subnetMaskLength < 0 || subnetMaskLength > 32) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }

        IpAddress subnetStart;
        try {
            subnetStart = new IpAddress(subnetDescriptionParts[0]);
            long subnetStartBits = subnetStart.getBitRepresentation();
            byte shift = (byte) (32 - subnetMaskLength);
            long subnetMask = (subnetStartBits >> shift) << shift;
            if (subnetStartBits != subnetMask) {
                return false;
            }
        } catch (IllegalArgumentException ex) {
            return false;
        }

        return true;
    }



    private final IpAddress subnetIp;
    private final byte subnetAddressLength;
}
