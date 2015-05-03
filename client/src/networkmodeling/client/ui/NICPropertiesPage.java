package networkmodeling.client.ui;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
        ipTextField.getDocument().addDocumentListener(
            new IpChangeListener(ipTextField, applyButton));
        applyButton.setEnabled(false);
        applyButton.addActionListener(
            new IpAssignmentListener(clientAppModel, ipTextField, applyButton));

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
}
