package networkmodeling.client.ui;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import networkmodeling.client.Client;

public class ButtonToolbar extends JPanel {
    
    public ButtonToolbar(Client client) {
        this.client = client;
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        testNetwork = new JButton("Test network");
        
        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(connectButton);
        add(disconnectButton);
        add(testNetwork);
    }
    
    private final Client client;
    private final JButton connectButton;
    private final JButton disconnectButton;
    private final JButton testNetwork;
}
