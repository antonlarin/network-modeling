package networkmodeling.core;

public class IpAddress {
    public IpAddress(byte octet1, byte octet2, byte octet3, byte octet4) {
        this.octet1 = octet1;
        this.octet2 = octet2;
        this.octet3 = octet3;
        this.octet4 = octet4;
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
    
    
    private final byte octet1;
    private final byte octet2;
    private final byte octet3;
    private final byte octet4;
}
