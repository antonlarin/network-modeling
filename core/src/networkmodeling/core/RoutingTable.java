package networkmodeling.core;

import java.util.LinkedList;

public class RoutingTable {

    public RoutingTable() {
        routingTable = new LinkedList<>();
    }

    public void addRecord(RoutingTableRecord routingTableRecord) {
        routingTable.add(routingTableRecord);
    }

    public RoutingTableRecord getRouteForIp(IpAddress ipAddress) {
        for (RoutingTableRecord routingRecord : routingTable) {
            if (routingRecord.correspondsToAddress(ipAddress)) {
                return routingRecord;
            }
        }
        return null;
    }



    private final LinkedList<RoutingTableRecord> routingTable;
}
