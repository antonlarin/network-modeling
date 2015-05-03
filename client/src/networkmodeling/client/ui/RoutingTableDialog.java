package networkmodeling.client.ui;

import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class RoutingTableDialog extends JDialog {

    public RoutingTableDialog(ClientAppModel clientAppModel, JFrame parent) {
        super(parent, "Edit routing table", false);
        routingTableView = new JTable();
        applyButton = new JButton("Apply changes");

        setupDialog();
    }



    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                RoutingTableDialog dialog = new RoutingTableDialog(null, null);
                dialog.setVisible(true);
            }
        });
    }



    private void setupDialog() {
        JScrollPane tablePane = new JScrollPane(routingTableView);
        tablePane.setPreferredSize(new Dimension(400, 300));
        routingTableView.setFillsViewportHeight(true);

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addComponent(tablePane)
            .addComponent(applyButton)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addComponent(tablePane)
            .addComponent(applyButton)
        );

        setLayout(layout);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



    private final JTable routingTableView;
    private final JButton applyButton;
}
