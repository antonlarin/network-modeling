package networkmodeling.client.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonToolbar extends JPanel {
    
    public ButtonToolbar(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
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
            clientAppModel.getClientDaemon().connectToServer();
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
        }
    }
    
    private class DisconnectActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientAppModel.getClientDaemon().disconnect();
            clientAppModel.resetClientDaemon();
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
        }
    }
    
    private class TestNetworkActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientAppModel.getVisualModel().GetModel().TestNetwork();
        }
    }
    
    private ClientAppModel clientAppModel;
    private final JButton connectButton;
    private final JButton disconnectButton;
    private final JButton testNetwork;
}
