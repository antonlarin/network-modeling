package networkmodeling.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;


public class IpAssignmentListener implements ActionListener {

    public IpAssignmentListener(ClientAppModel clientAppModel,
        JTextField ipTextField, JButton actionButton) {
        this.clientAppModel = clientAppModel;
        this.ipTextField = ipTextField;
        this.actionButton = actionButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clientAppModel.setIpForSelectedDevice(ipTextField.getText());
        actionButton.setEnabled(false);
    }



    private final ClientAppModel clientAppModel;
    private final JTextField ipTextField;
    private final JButton actionButton;
}