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

public class AddRouterDialog extends JDialog {

    public AddRouterDialog(WindowManager windowManager, JFrame parent,
        Point2D.Double newNodeLocation) {
        super(parent, "Add router", false);
        this.windowManager = windowManager;
        this.newNodeLocation = newNodeLocation;
        this.ipTextField = new JTextField("192.168.0.1");
        this.portsCountTextField = new JTextField("8");
        editRoutingTableButton = new JButton("Edit routing table");
        addButton = new JButton("Add router");
        cancelButton = new JButton("Cancel");

        setupPage();
    }



    // Method for testing layout of the dialog
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                AddRouterDialog dialog = new AddRouterDialog(null, null, null);
                dialog.setVisible(true);
            }
        });
    }



    private void setupPage() {
        JLabel ipTitleLabel = new JLabel("IP address:");
        JLabel portsCountTitleLabel = new JLabel("Ports count:");
        PortsCountChangeListener listener = new PortsCountChangeListener(
            portsCountTextField, addButton);
        portsCountTextField.getDocument().addDocumentListener(listener);
        editRoutingTableButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                windowManager.showRoutingTableEditDialog();
            }
        });
        addButton.addActionListener(new AddRouterListener());
        cancelButton.addActionListener(new CancelDeviceAdditionListener(this));

        GroupLayout layout = new GroupLayout(this.getContentPane());
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(
                        GroupLayout.Alignment.LEADING)
                        .addComponent(ipTitleLabel)
                        .addComponent(portsCountTitleLabel))
                    .addGroup(layout.createParallelGroup(
                        GroupLayout.Alignment.TRAILING)
                        .addComponent(ipTextField)
                        .addComponent(portsCountTextField)))
                .addComponent(editRoutingTableButton,
                    GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(addButton)
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
                    .addComponent(portsCountTitleLabel)
                    .addComponent(portsCountTextField))
                .addComponent(editRoutingTableButton)
                .addGroup(layout.createParallelGroup()
                    .addComponent(addButton)
                    .addComponent(cancelButton))
        );

        this.setLayout(layout);
        pack();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }



    private final WindowManager windowManager;
    private final Point2D.Double newNodeLocation;
    private final JTextField ipTextField;
    private final JTextField portsCountTextField;
    private final JButton editRoutingTableButton;
    private final JButton addButton;
    private final JButton cancelButton;



    private class AddRouterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            windowManager.getClientAppModel().addRouterWithProperties(
                ipTextField.getText(), portsCountTextField.getText(),
                newNodeLocation);
            AddRouterDialog.this.dispose();
        }
    }
}
