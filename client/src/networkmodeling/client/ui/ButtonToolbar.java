package networkmodeling.client.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import networkmodeling.client.Client;

public class ButtonToolbar extends JPanel {
    
    public ButtonToolbar(Client client) {
        this.client = client;
        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ConnectActionListener());
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(new DisconnectActionListener());
        testNetwork = new JButton("Test network");
        
        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(connectButton);
        add(disconnectButton);
        add(testNetwork);
    }
    
    private class ConnectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            client.connectToServer();
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
        }
    }
    
    private class DisconnectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            client.disconnect();
            client = new Client();
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
        }
    }
    
    private class TestNetworkActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            client.GetVisualModel().GetModel().TestNetwork();
        }
    }
    
    private Client client;
    private final JButton connectButton;
    private final JButton disconnectButton;
    private final JButton testNetwork;
}
