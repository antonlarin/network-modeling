package networkmodeling.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import networkmodeling.core.IpAddress;

public class AddNICDialog extends JDialog {

    public AddNICDialog(ClientAppModel clientAppModel, JFrame parent,
        Point2D.Double newNodeLocation) {
        super(parent, "Add NIC", false);
        this.clientAppModel = clientAppModel;
        this.newNodeLocation = newNodeLocation;
        ipTextField = new JTextField("192.168.0.100");
        gatewayTextField = new JTextField("192.168.0.1");
        addButton = new JButton("Add NIC");
        cancelButton = new JButton("Cancel");
        
        setupDialog();
    }



    // Method for testing layout of the dialog
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                AddNICDialog dialog = new AddNICDialog(null, null, null);
                dialog.setVisible(true);
            }
        });
    }



    private void setupDialog() {
        JLabel ipTitleLabel = new JLabel("IP address:");
        JLabel gatewayTitleLabel = new JLabel("Gateway IP:");
        IpGatewayChangeListener listener = new IpGatewayChangeListener();
        ipTextField.getDocument().addDocumentListener(listener);
        gatewayTextField.getDocument().addDocumentListener(listener);
        addButton.addActionListener(new AddNICListener());
        cancelButton.addActionListener(new CancelNicAdditionListener());

        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.LEADING)
                    .addComponent(ipTitleLabel)
                    .addComponent(gatewayTitleLabel)
                    .addComponent(addButton))
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.TRAILING)
                    .addComponent(ipTextField)
                    .addComponent(gatewayTextField)
                    .addComponent(cancelButton))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.BASELINE)
                    .addComponent(ipTitleLabel)
                    .addComponent(ipTextField))
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.BASELINE)
                    .addComponent(gatewayTitleLabel)
                    .addComponent(gatewayTextField))
                .addGroup(layout.createParallelGroup()
                    .addComponent(addButton)
                    .addComponent(cancelButton))
        );

        this.setLayout(layout);
        pack();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



    private final ClientAppModel clientAppModel;
    private final Point2D.Double newNodeLocation;
    private final JTextField ipTextField;
    private final JTextField gatewayTextField;
    private final JButton addButton;
    private final JButton cancelButton;



    private class IpGatewayChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            handleUpdate();
        }
        
        private void handleUpdate() {
            String ip = ipTextField.getText();
            String gateway = gatewayTextField.getText();
            if (IpAddress.isValid(ip) && IpAddress.isValid(gateway)) {
                addButton.setEnabled(true);
            } else {
                addButton.setEnabled(false);
            }
        }
    }
    
    private class AddNICListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            clientAppModel.addNICWithProperties(ipTextField.getText(),
                gatewayTextField.getText(), newNodeLocation);
            AddNICDialog.this.dispose();
        }
    }
    
    private class CancelNicAdditionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AddNICDialog.this.dispose();
        }
    }
}
