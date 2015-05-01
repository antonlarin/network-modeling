package networkmodeling.client.ui;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import networkmodeling.core.NIC;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class NICPropertiesPage extends JPanel {
    
    public NICPropertiesPage() {
        macLabel = new JLabel("");
        ipTextField = new JTextField("<nic-ip>");
        gatewayIpTextField = new JTextField("<gateway-ip>");
        applyButton = new JButton("Apply changes");

        setupPage();
    }
    
    public void associateNode(NetworkGraphNode node) {
        associatedDevice = (NIC) node.getNodeDevice();
        macLabel.setText(associatedDevice.getMacAddress().toString());
        ipTextField.setText(associatedDevice.getIpAddress().toString());
    }



    private void setupPage() {
        JLabel deviceTypeLabel = new JLabel("Device type: NIC");
        JLabel macTitleLabel = new JLabel("MAC address:");
        JLabel ipTitleLabel = new JLabel("IP address:");
        JLabel gatewayIpTitleLabel = new JLabel("Gateway IP:");
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
                        .addComponent(gatewayIpTitleLabel))
                    .addGroup(layout.createParallelGroup()
                        .addComponent(macLabel)
                        .addComponent(ipTextField)
                        .addComponent(gatewayIpTextField)))
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
                    .addComponent(gatewayIpTitleLabel)
                    .addComponent(gatewayIpTextField))
                .addComponent(applyButton)
        );
        
        controlsContainer.setLayout(layout);
        setLayout(new BorderLayout());
        add(controlsContainer, BorderLayout.NORTH);
        
        ipTextField.getDocument().addDocumentListener(new IpChangeListener());
    }

    private final JLabel macLabel;
    private final JTextField ipTextField;
    private final JTextField gatewayIpTextField;
    private final JButton applyButton;
    private NIC associatedDevice;



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
            if (!newIp.equals(associatedDevice.getIpAddress().toString())) {
                applyButton.setEnabled(true);
            } else {
                applyButton.setEnabled(false);
            }
        }
    }
}
