package networkmodeling.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

public class MacAddress implements Serializable {
    public MacAddress() {
        this.octet1 = 0x00;
        this.octet2 = 0x00;
        this.octet3 = 0x00;
        this.octet4 = 0x00;
        this.octet5 = 0x00;
        this.octet6 = 0x00;
    }
    
    public MacAddress(short octet1, short octet2, short octet3,
            short octet4, short octet5, short octet6) {
        this.octet1 = octet1;
        this.octet2 = octet2;
        this.octet3 = octet3;
        this.octet4 = octet4;
        this.octet5 = octet5;
        this.octet6 = octet6;
    }

    
    public static MacAddress getBroadcastAddress() {
        short octet = (short) 0xFF;
        return new MacAddress(octet, octet, octet, octet, octet, octet);
    }
    
    public static MacAddress getRandomAddress() {
        Random generator = new Random();
        
        MacAddress newMac;
        do {
            short octet1 = (short) generator.nextInt(256);
            short octet2 = (short) generator.nextInt(256);
            short octet3 = (short) generator.nextInt(256);
            short octet4 = (short) generator.nextInt(256);
            short octet5 = (short) generator.nextInt(256);
            short octet6 = (short) generator.nextInt(256);
            newMac = new MacAddress(octet1, octet2, octet3, octet4,
                octet5, octet6);
        } while (assigned(newMac));
        assignedAddresses.push(newMac);
        return newMac;
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
    

    private static boolean assigned(MacAddress newMac) {
        if (assignedAddresses == null) {
            assignedAddresses = new LinkedList<>();
            return false;
        } else {
            return assignedAddresses.contains(newMac);
        }
    }
    
    private static LinkedList<MacAddress> assignedAddresses;
    
    private final short octet1;
    private final short octet2;
    private final short octet3;
    private final short octet4;
    private final short octet5;
    private final short octet6;
}
