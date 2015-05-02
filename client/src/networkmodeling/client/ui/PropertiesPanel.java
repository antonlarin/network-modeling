package networkmodeling.client.ui;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import networkmodeling.core.Hub;
import networkmodeling.core.NIC;
import networkmodeling.core.NetworkDevice;
import networkmodeling.core.Switch;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class PropertiesPanel extends JPanel {
    
    public PropertiesPanel(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
        emptyProps = new EmptyPropertiesPage();
        nicProps = new NICPropertiesPage(clientAppModel);
        hubProps = new HubPropertiesPage();
        switchProps = new SwitchPropertiesPage();
        
        setLayout(new CardLayout());
        
        add(emptyProps, "EmptyProps");
        add(switchProps, "SwitchProps");
        add(nicProps, "NicProps");
        add(hubProps, "HubProps");
        
        setBorder(BorderFactory.createTitledBorder("Device properties:"));
    }



    private final ClientAppModel clientAppModel;
    private final EmptyPropertiesPage emptyProps;
    private final NICPropertiesPage nicProps;
    private final HubPropertiesPage hubProps;
    private final SwitchPropertiesPage switchProps;
    
    
    
    public class SelectedNodeChangeListener
        implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ClientAppModel clientAppModel = (ClientAppModel) evt.getSource();
            CardLayout layout = (CardLayout) getLayout();

            NetworkGraphNode selectedNode = clientAppModel.getSelectedNode();
            if (selectedNode == null) {
                layout.show(PropertiesPanel.this, "EmptyProps");
            } else {
                NetworkDevice selectedDevice =
                    clientAppModel.getSelectedNode().getNodeDevice();
                if (selectedDevice instanceof NIC) {
                    layout.show(PropertiesPanel.this, "NicProps");
                    nicProps.associateNode(selectedNode);
                } else if (selectedDevice instanceof Hub) {
                    layout.show(PropertiesPanel.this, "HubProps");
                    hubProps.associateNode(selectedNode);
                } else if (selectedDevice instanceof Switch) {
                    layout.show(PropertiesPanel.this, "SwitchProps");
                    switchProps.associateNode(selectedNode);
//                } else { // if (selectedDevice instanceof Router)
//                    layout.show(PropertiesPanel.this, "RouterProps");
//                    routerProps.associateNode(selectedNode);
                }
            }
        }
    }
}
