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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AddHubDialog extends JDialog {
    public AddHubDialog(ClientAppModel clientAppModel, JFrame parent,
        Point2D.Double newNodeLocation) {
        super(parent, "Add hub", false);
        this.clientAppModel = clientAppModel;
        this.newNodeLocation = newNodeLocation;
        portsCountTextField = new JTextField("8");
        addButton = new JButton("Add hub");
        cancelButton = new JButton("Cancel");
        
        setupDialog();
    }



    // Method for testing layout of the dialog
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                AddHubDialog dialog = new AddHubDialog(null, null, null);
                dialog.setVisible(true);
            }
        });
    }



    private void setupDialog() {
        JLabel portsCountTitleLabel = new JLabel("Ports count:");
        PortsCountChangeListener listener = new PortsCountChangeListener();
        portsCountTextField.getDocument().addDocumentListener(listener);
        addButton.addActionListener(new AddHubListener());
        cancelButton.addActionListener(new CancelHubAdditionListener());

        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.LEADING)
                    .addComponent(portsCountTitleLabel)
                    .addComponent(addButton))
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.TRAILING)
                    .addComponent(portsCountTextField)
                    .addComponent(cancelButton))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(
                    GroupLayout.Alignment.BASELINE)
                    .addComponent(portsCountTitleLabel)
                    .addComponent(portsCountTextField))
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
    private final JTextField portsCountTextField;
    private final JButton addButton;
    private final JButton cancelButton;



    private class PortsCountChangeListener implements DocumentListener {

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
                    addButton.setEnabled(true);
                    return;
                }
            } catch (NumberFormatException ex) {}
            addButton.setEnabled(false);
        }
    }
    
    private class AddHubListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            clientAppModel.addHubWithProperties(portsCountTextField.getText(),
                newNodeLocation);
            AddHubDialog.this.dispose();
        }
    }
    
    private class CancelHubAdditionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AddHubDialog.this.dispose();
        }
    }
}
