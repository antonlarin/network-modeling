package networkmodeling.client.ui;

import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;
import networkmodeling.core.IpAddress;
import networkmodeling.core.RoutingTable;
import networkmodeling.core.RoutingTableRecord;
import networkmodeling.core.SubnetIpDescription;

public class RoutingTableModel extends AbstractTableModel {

    public RoutingTableModel(RoutingTable routingTable) {
        this.subnetAddresses = new LinkedList<>();
        this.nextHopIps = new LinkedList<>();
        this.portIndices = new LinkedList<>();

        if (routingTable != null) {
            fillTable(routingTable);
        }
    }

    @Override
    public int getRowCount() {
        return subnetAddresses.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            return subnetAddresses.get(rowIndex);
        case 1:
            return nextHopIps.get(rowIndex);
        default: // case 2:
            return portIndices.get(rowIndex);
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case 0:
            subnetAddresses.set(rowIndex, (String) value);
            break;
        case 1:
            nextHopIps.set(rowIndex, (String) value);
            break;
        default: // case 2:
            portIndices.set(rowIndex, (Integer) value);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
        case 0:
            return "Subnet";
        case 1:
            return "Next hop IP";
        default: // case 3:
            return "Port #";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void insertRow() {
        subnetAddresses.push("");
        nextHopIps.push("");
        portIndices.push(1);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public boolean isTableValid() {
        for (int i = 0; i < getRowCount(); ++i) {
            if (!SubnetIpDescription.isValid(subnetAddresses.get(i))) {
                return false;
            }
            if (!IpAddress.isValid(nextHopIps.get(i))) {
                return false;
            }
            if (portIndices.get(i) < 0) {
                return false;
            }
        }

        return true;
    }



    private void fillTable(RoutingTable routingTable) {
        for (int i = 0; i < routingTable.getRecordsCount(); ++i) {
            RoutingTableRecord record = routingTable.getRecordWithIndex(i);
            subnetAddresses.push(record.getSubnetAddress().toString());
            nextHopIps.push(record.getHopIp().toString());
            portIndices.push(record.getPortIndex());
        }
    }



    private final LinkedList<String> subnetAddresses;
    private final LinkedList<String> nextHopIps;
    private final LinkedList<Integer> portIndices;
}
