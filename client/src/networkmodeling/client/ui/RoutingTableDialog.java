package networkmodeling.client.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import networkmodeling.core.Router;

public class RoutingTableDialog extends JDialog {

    public RoutingTableDialog(ClientAppModel clientAppModel, JFrame parent,
        boolean forExistingRouter) {
        super(parent, "Edit routing table", false);
        routingTableView = new JTable();
        if (forExistingRouter) {
            Router selectedRouter =
                (Router) clientAppModel.getSelectedNode().getNodeDevice();
            routingTableModel = new RoutingTableModel(
                selectedRouter.getRoutingTable());
            routerPortsCount = selectedRouter.getPortsCount();
        } else {
            routingTableModel = new RoutingTableModel(null);
            // TODO change 8 here
            routerPortsCount = 8;
        }
        applyButton = new JButton("Apply changes");
        addRecordButton = new JButton("Add record");

        setupDialog();
    }



    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                RoutingTableDialog dialog =
                    new RoutingTableDialog(null, null, false);
                dialog.setVisible(true);
            }
        });
    }



    private void setupDialog() {
        JScrollPane tablePane = new JScrollPane(routingTableView);
        tablePane.setPreferredSize(new Dimension(400, 300));
        routingTableView.setFillsViewportHeight(true);
        routingTableView.setModel(routingTableModel);
        JComboBox<Integer> portIndexSelector = new JComboBox<>();
        for (int i = 0; i < routerPortsCount; ++i) {
            portIndexSelector.addItem(i);
        }
        routingTableView.getColumnModel().getColumn(2).setCellEditor(
            new DefaultCellEditor(portIndexSelector));
        routingTableModel.addTableModelListener(new RoutingTableListener());

        addRecordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                routingTableModel.insertRow();
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(addRecordButton)
                .addComponent(tablePane)
                .addComponent(applyButton)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(addRecordButton)
                .addComponent(tablePane)
                .addComponent(applyButton)
        );

        setLayout(layout);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



    private final RoutingTableModel routingTableModel;
    private int routerPortsCount;
    private final JTable routingTableView;
    private final JButton applyButton;
    private final JButton addRecordButton;



    private class RoutingTableListener implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            if (routingTableModel.isTableValid()) {
                applyButton.setEnabled(true);
            } else {
                applyButton.setEnabled(false);
            }
        }
    }
}
