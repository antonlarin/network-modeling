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
                for (int i = 0; i < 1000; ++i) {
                    testDialog.appendString(
                        "Quick brown fox jumps over the lazy dog.\n");
                }
                testDialog.setLocationRelativeTo(null);
                testDialog.setVisible(true);
            }
        });
    }



    private void setupDialog() {
        JScrollPane logPane = new JScrollPane(testLogTextArea);
        logPane.setPreferredSize(new Dimension(350, 300));
        logPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 4, 4, 4),
            BorderFactory.createLineBorder(Color.lightGray)));
        testLogTextArea.setEditable(false);
        if (clientAppModel != null) {
            clientAppModel.addPropertyChangeListener("networkTestResult",
                new NetworkTestResultListener());
        }
        add(logPane, BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    private void appendString(String line) {
        testLogTextArea.append(line);
    }



    private final ClientAppModel clientAppModel;
    private final JTextArea testLogTextArea;



    private class NetworkTestResultListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String[] testNetworkLog =
                clientAppModel.getNetworkTestResult().getTestLog();
            for (int i = 0; i < testNetworkLog.length; ++i) {
                testLogTextArea.append(testNetworkLog[i]);
            }
        }
    }
}
