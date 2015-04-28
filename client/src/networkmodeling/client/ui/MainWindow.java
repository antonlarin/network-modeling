package networkmodeling.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import networkmodeling.client.Client;

public class MainWindow extends JFrame {
    
    public MainWindow(Client client) {
        this.client = client;
        toolbarPanel = new ButtonToolbar(client);
        JScrollPane palettePane = arrangeDevicePalette();
        propertiesPanel = new PropertiesPanel(client);
        diagramPanel = new DiagramPanel();
        
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.PAGE_AXIS));
        leftMenu.add(palettePane);
        leftMenu.add(propertiesPanel);
        leftMenu.setPreferredSize(new Dimension(200, 50));
        
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.NORTH);
        add(leftMenu, BorderLayout.WEST);
        add(diagramPanel, BorderLayout.CENTER);
    }

    
    private JScrollPane arrangeDevicePalette() {
        JList devicePalette = new JList();
        devicePalette.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        devicePalette.setLayoutOrientation(JList.VERTICAL);
        
        DefaultListModel devices = new DefaultListModel();
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
                Client client = new Client();
                MainWindow mainWindow = new MainWindow(client);
                mainWindow.pack();
                mainWindow.setVisible(true);
            }
        });
    }
    
    private final Client client;
    private final ButtonToolbar toolbarPanel;
//    private final JList devicePalette;
    private final PropertiesPanel propertiesPanel;
    private final DiagramPanel diagramPanel;
}
