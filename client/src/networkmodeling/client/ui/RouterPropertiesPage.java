package networkmodeling.client.ui;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import networkmodeling.core.Router;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class RouterPropertiesPage extends JPanel {

    public RouterPropertiesPage(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
        macLabel = new JLabel("");
        ipTextField = new JTextField("");
        portsCountLabel = new JLabel("");
        applyButton = new JButton("Apply changes");

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
                        .addComponent(portsCountTitleLabel)
                        .addComponent(ipTitleLabel))
                    .addGroup(layout.createParallelGroup()
                        .addComponent(macLabel)
                        .addComponent(portsCountLabel)
                        .addComponent(ipTextField)))
                .addComponent(applyButton)
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
        );

        controlsContainer.setLayout(layout);
        setLayout(new BorderLayout());
        add(controlsContainer, BorderLayout.NORTH);
    }

    private final JLabel macLabel;
    private final JTextField ipTextField;
    private final JLabel portsCountLabel;
    private final JButton applyButton;
    private Router associatedDevice;
    private final ClientAppModel clientAppModel;
}
