package networkmodeling.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class NetworkTestDialog extends JDialog {

    public NetworkTestDialog(ClientAppModel clientAppModel, JFrame parent) {
        super(parent, "Network test log", false);
        this.clientAppModel = clientAppModel;
        testLogTextArea = new JTextArea();

        setupDialog();
    }



    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                NetworkTestDialog testDialog =
                    new NetworkTestDialog(null, null);
                testDialog.setLocationRelativeTo(null);
                testDialog.setVisible(true);
            }
        });
    }



    private void setupDialog() {
        JScrollPane logPane = new JScrollPane(testLogTextArea);
        logPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 4, 4, 4),
            BorderFactory.createLineBorder(Color.lightGray)));
        testLogTextArea.setEditable(false);
        testLogTextArea.setPreferredSize(new Dimension(350, 300));
        clientAppModel.addPropertyChangeListener("networkTestResult",
            new NetworkTestResultListener());
        add(logPane, BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }



    private final ClientAppModel clientAppModel;
    private final JTextArea testLogTextArea;



    private class NetworkTestResultListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String[] testNetworkLog =
                clientAppModel.getNetworkTestResult().getTestLog();
            for (String line : testNetworkLog) {
                testLogTextArea.append(line);
            }
            if (clientAppModel.getNetworkTestResult().getTestResult()) {
                testLogTextArea.append("Result: test failed.");
            } else {
                testLogTextArea.append("Result: test passed.");
            }
        }
    }
}
