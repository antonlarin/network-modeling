package networkmodeling.client.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import networkmodeling.core.IpAddress;
import networkmodeling.core.Router;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class RouterPropertiesPage extends JPanel {

    public RouterPropertiesPage(WindowManager windowManager) {
        this.windowManager = windowManager;
        macLabel = new JLabel("");
        ipTextField = new JTextField("");
        portsCountLabel = new JLabel("");
        applyButton = new JButton("Apply changes");
        editRoutingTableButton = new JButton("Edit routing table");

        setupPage();
    }

    public void associateNode(NetworkGraphNode node) {
        associatedDevice = (Router) node.getNodeDevice();
        macLabel.setText(associatedDevice.getMacAddress().toString());
        ipTextField.setText(associatedDevice.getIpAddress().toString());
        portsCountLabel.setText(
            String.format("%d",associatedDevice.getPortsCount()));
    }



    private void setupPage() {
        JLabel deviceTypeLabel = new JLabel("Device type: Router");
        JLabel macTitleLabel = new JLabel("MAC address:");
        JLabel ipTitleLabel = new JLabel("IP address:");
        JLabel portsCountTitleLabel = new JLabel("Ports count:");
        ipTextField.getDocument().addDocumentListener(
            new IpChangeListener());
        applyButton.setEnabled(false);
        applyButton.addActionListener(new IpAssignmentListener());
        editRoutingTableButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                windowManager.showRoutingTableEditDialog(true,
                    associatedDevice.getPortsCount());
            }
        });

        windowManager.getClientAppModel().addPropertyChangeListener(
            "stashedRoutingTable", new RoutingTableListener());

        JPanel controlsContainer = new JPanel();
        GroupLayout layout = new GroupLayout(controlsContainer);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(deviceTypeLabel)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addComponent(macTitleLabel)
                        .addComponent(portsCountTitleLabel)
                        .addComponent(ipTitleLabel))
                    .addGroup(layout.createParallelGroup()
                        .addComponent(macLabel)
                        .addComponent(portsCountLabel)
                        .addComponent(ipTextField)))
                .addComponent(applyButton)
                .addComponent(editRoutingTableButton)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(deviceTypeLabel)
                .addGroup(layout.createParallelGroup()
                    .addComponent(macTitleLabel)
                    .addComponent(macLabel))
                .addGroup(layout.createParallelGroup()
                    .addComponent(portsCountTitleLabel)
                    .addComponent(portsCountLabel))
                .addGroup(layout.createParallelGroup()
                    .addComponent(ipTitleLabel)
                    .addComponent(ipTextField))
                .addComponent(applyButton)
                .addComponent(editRoutingTableButton)
        );

        controlsContainer.setLayout(layout);
        setLayout(new BorderLayout());
        add(controlsContainer, BorderLayout.NORTH);
    }

    private final JLabel macLabel;
    private final JTextField ipTextField;
    private final JLabel portsCountLabel;
    private final JButton applyButton;
    private final JButton editRoutingTableButton;
    private Router associatedDevice;
    private final WindowManager windowManager;



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
            String newIp = ipTextField.getText();
            if ((!newIp.equals(associatedDevice.getIpAddress().toString()) &&
                IpAddress.isValid(newIp))) {
                applyButton.setEnabled(true);
            } else {
                applyButton.setEnabled(false);
            }
        }
    }

    private class IpAssignmentListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String newIp = ipTextField.getText();
            if (!newIp.equals(associatedDevice.getIpAddress().toString())) {
                windowManager.getClientAppModel().setIpForSelectedDevice(
                    ipTextField.getText());
            }
            applyButton.setEnabled(false);
        }
    }

    private class RoutingTableListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            windowManager.getClientAppModel().
                setStashedRoutingTableForSelectedRouter();
        }
    }
}
