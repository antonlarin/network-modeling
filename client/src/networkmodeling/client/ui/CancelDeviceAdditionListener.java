package networkmodeling.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;


public class CancelDeviceAdditionListener implements ActionListener {

    public CancelDeviceAdditionListener(JDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dialog.dispose();
    }

    private final JDialog dialog;
}