package networkmodeling.client.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SwitchPropertiesPage extends JPanel {
    
    public SwitchPropertiesPage() {
        JLabel titleLabel = new JLabel("Switch properties:");
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
