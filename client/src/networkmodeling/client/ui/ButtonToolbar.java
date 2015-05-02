package networkmodeling.client.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class ButtonToolbar extends JPanel {
    
    public ButtonToolbar(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        testNetworkButton = new JButton("Test network");
        removeButton = new JButton("Remove");

        setupPage();
    }

    private void setupPage() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 20));
        connectButton.addActionListener(new ConnectListener());
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(new DisconnectListener());
        testNetworkButton.addActionListener(new TestNetworkListener());
        removeButton.addActionListener(new RemoveElementListener());
        removeButton.setEnabled(false);
        ElementForRemovalListener efrListener = new ElementForRemovalListener();
        clientAppModel.addPropertyChangeListener("selectedNode", efrListener);
        clientAppModel.addPropertyChangeListener("selectedEdge", efrListener);

        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(connectButton);
        add(disconnectButton);
        add(testNetworkButton);
        add(separator);
        add(removeButton);
    }



    private ClientAppModel clientAppModel;
    private final JButton connectButton;
    private final JButton disconnectButton;
    private final JButton testNetworkButton;
    private final JButton removeButton;



    private class ConnectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientAppModel.getClientDaemon().connectToServer();
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(true);
        }
    }
    
    private class DisconnectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientAppModel.getClientDaemon().disconnect();
            clientAppModel.resetClientDaemon();
            connectButton.setEnabled(true);
            disconnectButton.setEnabled(false);
        }
    }
    
    private class TestNetworkListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            clientAppModel.getVisualModel().GetModel().TestNetwork();
        }
    }
    
    private class ElementForRemovalListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (clientAppModel.getSelectedEdge() == null &&
                clientAppModel.getSelectedNode() == null) {
                removeButton.setEnabled(false);
            } else {
                removeButton.setEnabled(true);
            }
        }
    }
    
    private class RemoveElementListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (clientAppModel.getSelectedEdge() != null) {                
                clientAppModel.removeSelectedEdge();
            } else if (clientAppModel.getSelectedNode() != null) {
                clientAppModel.removeSelectedNode();
            }
        }
    }
}
