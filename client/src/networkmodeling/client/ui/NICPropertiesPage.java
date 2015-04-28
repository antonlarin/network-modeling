package networkmodeling.client.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NICPropertiesPage extends JPanel {
    
    public NICPropertiesPage() {
        JLabel titleLabel = new JLabel("NIC properties:");
        JLabel macTitleLabel = new JLabel("MAC address:");
        macLabel = new JLabel("");
        JLabel ipTitleLabel = new JLabel("IP address:");
        ipTextField = new JTextField("");
        applyButton = new JButton("Apply");
        applyButton.setEnabled(false);
        
        add(titleLabel);
        add(macTitleLabel);
        add(macLabel);
        add(ipTitleLabel);
        add(ipTextField);
        add(applyButton);
        
    }
    
    private final JLabel macLabel;
    private final JTextField ipTextField;
    private final JButton applyButton;
}
