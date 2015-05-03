package networkmodeling.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import networkmodeling.core.IpAddress;
import networkmodeling.core.RoutingTable;

public class AddRouterDialog extends JDialog {

    public AddRouterDialog(WindowManager windowManager, JFrame parent,
        Point2D.Double newNodeLocation) {
        super(parent, "Add router", false);
        this.windowManager = windowManager;
        this.newNodeLocation = newNodeLocation;
        this.ipTextField = new JTextField("192.168.0.1");
        this.portsCountTextField = new JTextField("8");
        editRoutingTableButton = new JButton("Edit routing table");
        addButton = new JButton("Add router");
        cancelButton = new JButton("Cancel");

        setupPage();
    }



    // Method for testing layout of the dialog
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                AddRouterDialog dialog = new AddRouterDialog(null, null, null);
                dialog.setVisible(true);
            }
        });
    }



    private void setupPage() {
        JLabel ipTitleLabel = new JLabel("IP address:");
        JLabel portsCountTitleLabel = new JLabel("Ports count:");
        portsCountTextField.getDocument().addDocumentListener(
            new PortsCountChangeListener());
        ipTextField.getDocument().addDocumentListener(new IpChangeListener());
        editRoutingTableButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                windowManager.showRoutingTableEditDialog(false,
                    Integer.valueOf(portsCountTextField.getText()));
            }
        });
        addButton.addActionListener(new AddRouterListener());
        cancelButton.addActionListener(new CancelDeviceAdditionListener(this));

        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(
                        GroupLayout.Alignment.LEADING)
                        .addComponent(ipTitleLabel)
                        .addComponent(portsCountTitleLabel))
                    .addGroup(layout.createParallelGroup(
                        GroupLayout.Alignment.TRAILING)
                        .addComponent(ipTextField)
                        .addComponent(portsCountTextField)))
                .addComponent(editRoutingTableButton,
                    GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(addButton)
                    .addComponent(cancelButton))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.BASELINE)
                    .addComponent(ipTitleLabel)
                    .addComponent(ipTextField))
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.BASELINE)
                    .addComponent(portsCountTitleLabel)
                    .addComponent(portsCountTextField))
                .addComponent(editRoutingTableButton)
                .addGroup(layout.createParallelGroup()
                    .addComponent(addButton)
                    .addComponent(cancelButton))
        );

        this.setLayout(layout);
        pack();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



    private final WindowManager windowManager;
    private final Point2D.Double newNodeLocation;
    private final JTextField ipTextField;
    private final JTextField portsCountTextField;
    private final JButton editRoutingTableButton;
    private final JButton addButton;
    private final JButton cancelButton;



    private class AddRouterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            windowManager.getClientAppModel().addRouterWithProperties(
                ipTextField.getText(), portsCountTextField.getText(),
                newNodeLocation);
            AddRouterDialog.this.dispose();
        }
    }

    private class PortsCountChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            handleUpdate();
        }

        private void handleUpdate() {
            int currentPortsCount = -1;
            try {
                currentPortsCount =
                    Integer.valueOf(portsCountTextField.getText());

                RoutingTable stashedRoutingTable =
                    windowManager.getClientAppModel().getStashedRoutingTable();
                if (stashedRoutingTable != null) {
                    if (currentPortsCount < 0) {
                        addButton.setEnabled(false);
                        editRoutingTableButton.setEnabled(false);
                    } else if (currentPortsCount <=
                        stashedRoutingTable.getMaxUsedPortIndex()) {
                        addButton.setEnabled(false);
                        editRoutingTableButton.setEnabled(true);
                    } else {
                        addButton.setEnabled(true);
                        editRoutingTableButton.setEnabled(true);
                    }
                } else {
                    if (currentPortsCount < 0) {
                        addButton.setEnabled(false);
                        editRoutingTableButton.setEnabled(false);
                    } else {
                        addButton.setEnabled(true);
                        editRoutingTableButton.setEnabled(true);
                    }
                }
            } catch (NumberFormatException ex) {
                addButton.setEnabled(false);
                editRoutingTableButton.setEnabled(false);
            }
        }
    }

    private class IpChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            handleUpdate();
        }

        private void handleUpdate() {
            String ip = ipTextField.getText();
            if (!IpAddress.isValid(ip)) {
                addButton.setEnabled(false);
            } else {
                addButton.setEnabled(true);
            }
        }
    }
}
