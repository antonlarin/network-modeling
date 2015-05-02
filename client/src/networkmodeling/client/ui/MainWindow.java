package networkmodeling.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class MainWindow extends JFrame {
    
    public MainWindow(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
        toolbarPanel = new ButtonToolbar(clientAppModel);
        JScrollPane palettePane = arrangeDevicePalette();
        propertiesPanel = new PropertiesPanel(clientAppModel);
        diagramPanel = new DiagramPanel(clientAppModel);

        setTitle("Networkmodeling Client");
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new MainWindowListener(this));
        
        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.PAGE_AXIS));
        leftMenu.add(palettePane);
        leftMenu.add(propertiesPanel);
        leftMenu.setPreferredSize(new Dimension(220, 50));
        
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.NORTH);
        add(leftMenu, BorderLayout.WEST);
        add(diagramPanel, BorderLayout.CENTER);
        
        clientAppModel.addPropertyChangeListener("selectedNode",
            propertiesPanel.new SelectedNodeChangeListener());
    }
    
    public void cleanup() {
        clientAppModel.getClientDaemon().disconnect();
    }

    
    private JScrollPane arrangeDevicePalette() {
        JList<String> devicePalette = new JList<>();
        devicePalette.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        devicePalette.setLayoutOrientation(JList.VERTICAL);
        
        DefaultListModel<String> devices = new DefaultListModel<>();
        devices.addElement("NIC");
        devices.addElement("Hub");
        devices.addElement("Switch");
        devicePalette.setModel(devices);
        
        devicePalette.setDragEnabled(true);
        
        JScrollPane palettePane = new JScrollPane(devicePalette);
        palettePane.setBorder(
            BorderFactory.createTitledBorder("Device palette"));
        
        return palettePane;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientAppModel clientAppModel = new ClientAppModel();
                MainWindow mainWindow = new MainWindow(clientAppModel);
                mainWindow.pack();
                mainWindow.setVisible(true);
            }
        });
    }

    private final ClientAppModel clientAppModel;
    private final ButtonToolbar toolbarPanel;
    private final PropertiesPanel propertiesPanel;
    private final DiagramPanel diagramPanel;
    
    
    private class MainWindowListener implements WindowListener {
        
        public MainWindowListener(MainWindow window) {
            this.mainWindow = window;
        }

        @Override
        public void windowOpened(WindowEvent e) {}

        @Override
        public void windowClosing(WindowEvent e) {
            mainWindow.cleanup();
            mainWindow.dispose();
            System.exit(0);
        }

        @Override
        public void windowClosed(WindowEvent e) {}

        @Override
        public void windowIconified(WindowEvent e) {}

        @Override
        public void windowDeiconified(WindowEvent e) {}

        @Override
        public void windowActivated(WindowEvent e) {}

        @Override
        public void windowDeactivated(WindowEvent e) {}

        private final MainWindow mainWindow;
    }
}
