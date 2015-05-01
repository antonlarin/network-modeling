package networkmodeling.client.ui;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import networkmodeling.core.Hub;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class HubPropertiesPage extends JPanel {
    
    public HubPropertiesPage() {
        macLabel = new JLabel("");
        portsCountLabel = new JLabel("");
        
        setupPage();
    }

    public void associateNode(NetworkGraphNode node) {
        associatedDevice = (Hub) node.getNodeDevice();
        macLabel.setText(associatedDevice.getMacAddress().toString());
        portsCountLabel.setText(String.format("%d",
            associatedDevice.getPortsCount()));
    }



    private void setupPage() {
        JLabel deviceTypeLabel = new JLabel("Device type: Hub");
        JLabel macTitleLabel = new JLabel("MAC address:");
        JLabel portsCountTitleLabel = new JLabel("Ports count:");
        
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
                        .addComponent(portsCountTitleLabel))
                    .addGroup(layout.createParallelGroup()
                        .addComponent(macLabel)
                        .addComponent(portsCountLabel)))
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
        );
        
        controlsContainer.setLayout(layout);
        setLayout(new BorderLayout());
        add(controlsContainer, BorderLayout.NORTH);
    }
    
    private final JLabel macLabel;
    private final JLabel portsCountLabel;
    private Hub associatedDevice;
}
