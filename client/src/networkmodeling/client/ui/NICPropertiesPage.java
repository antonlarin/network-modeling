package networkmodeling.client.ui;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NICPropertiesPage extends JPanel {
    
    public NICPropertiesPage() {
        macLabel = new JLabel("");
        ipTextField = new JTextField("<nic-ip>");
        gatewayIpTextField = new JTextField("<gateway-ip>");
        applyButton = new JButton("Apply changes");

        setupPage();
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
    }

    private final JLabel macLabel;
    private final JTextField ipTextField;
    private final JTextField gatewayIpTextField;
    private final JButton applyButton;
}
