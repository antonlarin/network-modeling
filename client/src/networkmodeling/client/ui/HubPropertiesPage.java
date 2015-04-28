package networkmodeling.client.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HubPropertiesPage extends JPanel {
    
    public HubPropertiesPage() {
        JLabel titleLabel = new JLabel("Hub properties:");
        JLabel macTitleLabel = new JLabel("MAC address:");
        macLabel = new JLabel("");
        JLabel portsCountTitleLabel = new JLabel("Ports count:");
        portsCountLabel = new JLabel("");

        add(titleLabel);
        add(macTitleLabel);
        add(macLabel);
        add(portsCountTitleLabel);
        add(portsCountLabel);
    }
    
    private final JLabel macLabel;
    private final JLabel portsCountLabel;
}
