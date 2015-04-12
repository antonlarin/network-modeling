package networkmodeling.core;

public class MacAddress {
    public MacAddress() {
        this.octet1 = 0x00;
        this.octet2 = 0x00;
        this.octet3 = 0x00;
        this.octet4 = 0x00;
        this.octet5 = 0x00;
        this.octet6 = 0x00;
    }
    
    public MacAddress(byte octet1, byte octet2, byte octet3,
            byte octet4, byte octet5, byte octet6) {
        this.octet1 = octet1;
        this.octet2 = octet2;
        this.octet3 = octet3;
        this.octet4 = octet4;
        this.octet5 = octet5;
        this.octet6 = octet6;
    }

    
    public static MacAddress getBroadcastAddress() {
        byte octet = (byte) 0xFF;
        return new MacAddress(octet, octet, octet, octet, octet, octet);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MacAddress) {
            MacAddress other = (MacAddress) obj;
            return (this.octet1 == other.octet1 &&
                    this.octet2 == other.octet2 &&
                    this.octet3 == other.octet3 &&
                    this.octet4 == other.octet4 &&
                    this.octet5 == other.octet5 &&
                    this.octet6 == other.octet6);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.octet1;
        hash = 67 * hash + this.octet2;
        hash = 67 * hash + this.octet3;
        hash = 67 * hash + this.octet4;
        hash = 67 * hash + this.octet5;
        hash = 67 * hash + this.octet6;
        return hash;
    }
    
    public boolean isBroadcast() {
        return (this.octet1 == 0xFF &&
                this.octet2 == 0xFF &&
                this.octet3 == 0xFF &&
                this.octet4 == 0xFF &&
                this.octet5 == 0xFF &&
                this.octet6 == 0xFF);
    }
    
    public boolean isUnset() {
        return (this.octet1 == 0x00 &&
                this.octet2 == 0x00 &&
                this.octet3 == 0x00 &&
                this.octet4 == 0x00 &&
                this.octet5 == 0x00 &&
                this.octet6 == 0x00);
    }
    
    
    private final byte octet1;
    private final byte octet2;
    private final byte octet3;
    private final byte octet4;
    private final byte octet5;
    private final byte octet6;
}
