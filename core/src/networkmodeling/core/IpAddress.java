package networkmodeling.core;

import java.io.Serializable;

public class IpAddress implements Serializable {
    public IpAddress(short octet1, short octet2, short octet3, short octet4) {
        this.octet1 = octet1;
        this.octet2 = octet2;
        this.octet3 = octet3;
        this.octet4 = octet4;
    }
    
    public IpAddress(String ipstr) {
        if (!isValid(ipstr)) {
            throw new IllegalArgumentException();
        }
        
        String[] stringOctets = ipstr.split("\\.");
        octet1 = Short.valueOf(stringOctets[0]);
        octet2 = Short.valueOf(stringOctets[1]);
        octet3 = Short.valueOf(stringOctets[2]);
        octet4 = Short.valueOf(stringOctets[3]);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IpAddress) {
            IpAddress other = (IpAddress) obj;
            return (this.octet1 == other.octet1 &&
                    this.octet2 == other.octet2 &&
                    this.octet3 == other.octet3 &&
                    this.octet4 == other.octet4);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.octet1;
        hash = 89 * hash + this.octet2;
        hash = 89 * hash + this.octet3;
        hash = 89 * hash + this.octet4;
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("%d.%d.%d.%d", octet1, octet2, octet3, octet4);
    }



    public static boolean isValid(String ipAddress) {
        String[] stringOctets = ipAddress.split("\\.");

        if (stringOctets.length != 4) {
            return false;
        }

        for (String stringOctet : stringOctets) {
            short octet;
            try {
                octet = Short.valueOf(stringOctet);
            } catch (NumberFormatException ex) {
                return false;
            }
            
            if (octet < 0 || octet > 255) {
                return false;
            }
        }
        
        return true;
    }



    private final short octet1;
    private final short octet2;
    private final short octet3;
    private final short octet4;
}
