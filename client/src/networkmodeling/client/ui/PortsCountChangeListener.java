package networkmodeling.client.ui;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PortsCountChangeListener implements DocumentListener {

    public PortsCountChangeListener(JTextField portsCountTextField,
        JButton actionButton) {
        this.portsCountTextField = portsCountTextField;
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
        String portsCount = portsCountTextField.getText();
        try {
            int portsCountAsNumber = Integer.valueOf(portsCount);
            if (portsCountAsNumber > 0) {
                actionButton.setEnabled(true);
                return;
            }
        } catch (NumberFormatException ex) {}
        actionButton.setEnabled(false);
    }



    private final JTextField portsCountTextField;
    private final JButton actionButton;
}
