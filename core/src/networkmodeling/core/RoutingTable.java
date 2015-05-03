package networkmodeling.core;

import java.util.LinkedList;

public class RoutingTable {

    public RoutingTable() {
        routingTable = new LinkedList<>();
    }

    public void addRecord(RoutingTableRecord routingTableRecord) {
        routingTable.add(routingTableRecord);
    }

    public boolean removeRecord(RoutingTableRecord routingTableRecord) {
        return routingTable.remove(routingTableRecord);
    }

    public int getRecordsCount() {
        return routingTable.size();
    }

    public RoutingTableRecord getRecordWithIndex(int index) {
        return routingTable.get(index);
    }

    public void setRecordWithIndex(RoutingTableRecord record, int index) {
        routingTable.remove(index);
        routingTable.add(index, record);
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
