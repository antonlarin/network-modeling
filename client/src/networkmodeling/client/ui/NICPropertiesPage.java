package networkmodeling.client.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import networkmodeling.core.IpAddress;
import networkmodeling.core.NIC;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class NICPropertiesPage extends JPanel {

    public NICPropertiesPage(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
        macLabel = new JLabel("");
        ipTextField = new JTextField("");
        gatewayTextField = new JTextField("");
        applyButton = new JButton("Apply changes");

        setupPage();
    }

    public void associateNode(NetworkGraphNode node) {
        associatedDevice = (NIC) node.getNodeDevice();
        macLabel.setText(associatedDevice.getMacAddress().toString());
        ipTextField.setText(associatedDevice.getIpAddress().toString());
        gatewayTextField.setText(associatedDevice.getGateway().toString());
    }



    private void setupPage() {
        JLabel deviceTypeLabel = new JLabel("Device type: NIC");
        JLabel macTitleLabel = new JLabel("MAC address:");
        JLabel ipTitleLabel = new JLabel("IP address:");
        JLabel gatewayTitleLabel = new JLabel("Gateway IP:");
        IpGatewayChangeListener ipGatewayChangeListener =
            new IpGatewayChangeListener();
        ipTextField.getDocument().addDocumentListener(ipGatewayChangeListener);
        gatewayTextField.getDocument().addDocumentListener(
            ipGatewayChangeListener);
        applyButton.addActionListener(new IpGatewayAssignmentListener());
        applyButton.setEnabled(false);

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
                        .addComponent(ipTitleLabel)
                        .addComponent(gatewayTitleLabel))
                    .addGroup(layout.createParallelGroup()
                        .addComponent(macLabel)
                        .addComponent(ipTextField)
                        .addComponent(gatewayTextField)))
                .addComponent(applyButton)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(deviceTypeLabel)
                .addGroup(layout.createParallelGroup()
                    .addComponent(macTitleLabel)
                    .addComponent(macLabel))
                .addGroup(layout.createParallelGroup()
                    .addComponent(ipTitleLabel)
                    .addComponent(ipTextField))
                .addGroup(layout.createParallelGroup()
                    .addComponent(gatewayTitleLabel)
                    .addComponent(gatewayTextField))
                .addComponent(applyButton)
        );

        controlsContainer.setLayout(layout);
        setLayout(new BorderLayout());
        add(controlsContainer, BorderLayout.NORTH);
    }

    private final JLabel macLabel;
    private final JTextField ipTextField;
    private final JTextField gatewayTextField;
    private final JButton applyButton;
    private NIC associatedDevice;
    private final ClientAppModel clientAppModel;



    private class IpGatewayChangeListener implements DocumentListener {

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
            String newGateway = gatewayTextField.getText();
            if ((!newIp.equals(associatedDevice.getIpAddress().toString()) &&
                IpAddress.isValid(newIp)) ||
                (!newGateway.equals(associatedDevice.getGateway().toString()) &&
                IpAddress.isValid(newGateway))) {
                applyButton.setEnabled(true);
            } else {
                applyButton.setEnabled(false);
            }
        }
    }

    private class IpGatewayAssignmentListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String newIp = ipTextField.getText();
            String newGateway = gatewayTextField.getText();
            if (!newIp.equals(associatedDevice.getIpAddress().toString())) {
                clientAppModel.setIpForSelectedDevice(ipTextField.getText());
            }
            if (!newGateway.equals(associatedDevice.getGateway().toString())) {
                clientAppModel.setGatewayForSelectedDevice(
                    gatewayTextField.getText());
            }
            applyButton.setEnabled(false);
        }
    }
}
