package networkmodeling.client.ui;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import networkmodeling.client.Client;
import networkmodeling.core.Hub;
import networkmodeling.core.NIC;
import networkmodeling.core.NetworkDevice;
import networkmodeling.core.Switch;

public class PropertiesPanel extends JPanel {
    
    public PropertiesPanel(Client client) {
        this.client = client;
        emptyProps = new EmptyPropertiesPage();
        nicProps = new NICPropertiesPage();
        hubProps = new HubPropertiesPage();
        switchProps = new SwitchPropertiesPage();
        
        setLayout(new CardLayout());
        
        add(emptyProps, "EmptyProps");
        add(switchProps, "SwitchProps");
        add(nicProps, "NicProps");
        add(hubProps, "HubProps");
        
        setBorder(BorderFactory.createTitledBorder("Device properties:"));
    }



    private final Client client;
    private final EmptyPropertiesPage emptyProps;
    private final NICPropertiesPage nicProps;
    private final HubPropertiesPage hubProps;
    private final SwitchPropertiesPage switchProps;
    
    
    
    public class SelectedDeviceChangeListener
        implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            DiagramPanel diagramPanel = (DiagramPanel) evt.getSource();
            CardLayout layout = (CardLayout) getLayout();

            if (diagramPanel.getSelectedDevice() == null) {
                layout.show(PropertiesPanel.this, "EmptyProps");
            } else {
                NetworkDevice selectedDevice =
                    diagramPanel.getSelectedDevice().getNodeDevice();
                if (selectedDevice instanceof NIC) {
                    layout.show(PropertiesPanel.this, "NicProps");
                } else if (selectedDevice instanceof Hub) {
                    layout.show(PropertiesPanel.this, "HubProps");
                } else if (selectedDevice instanceof Switch) {
                    layout.show(PropertiesPanel.this, "SwitchProps");
//                } else { // if (selectedDevice instanceof Router)
//                    layout.show(PropertiesPanel.this, "RouterProps");
                }
            }
        }
    }
}
