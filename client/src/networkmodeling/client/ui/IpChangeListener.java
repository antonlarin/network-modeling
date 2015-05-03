package networkmodeling.client.ui;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import networkmodeling.core.IpAddress;

public class IpChangeListener implements DocumentListener {

    public IpChangeListener(JTextField ipTextField, JButton actionButton) {
        this.ipTextField = ipTextField;
        this.actionButton = actionButton;
    }

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
        if (!IpAddress.isValid(ip)) {
            actionButton.setEnabled(false);
        }
    }



    private final JTextField ipTextField;
    private final JButton actionButton;
}


