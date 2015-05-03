package networkmodeling.client.ui;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import networkmodeling.core.NetworkDevice;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class PropertiesPanel extends JPanel {

    public PropertiesPanel(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
        emptyProps = new EmptyPropertiesPage();
        nicProps = new NICPropertiesPage(clientAppModel);
        hubProps = new HubPropertiesPage();
        switchProps = new SwitchPropertiesPage();
        routerProps = new RouterPropertiesPage(clientAppModel);

        setLayout(new CardLayout());

        add(emptyProps, "EmptyProps");
        add(switchProps, "SwitchProps");
        add(nicProps, "NicProps");
        add(hubProps, "HubProps");
        add(routerProps, "RouterProps");

        setBorder(BorderFactory.createTitledBorder("Device properties:"));
    }



    private final ClientAppModel clientAppModel;
    private final EmptyPropertiesPage emptyProps;
    private final NICPropertiesPage nicProps;
    private final HubPropertiesPage hubProps;
    private final SwitchPropertiesPage switchProps;
    private final RouterPropertiesPage routerProps;



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
                switch (selectedDevice.getType()) {
                case NIC:
                    layout.show(PropertiesPanel.this, "NicProps");
                    nicProps.associateNode(selectedNode);
                    break;
                case  Hub:
                    layout.show(PropertiesPanel.this, "HubProps");
                    hubProps.associateNode(selectedNode);
                    break;
                case Switch:
                    layout.show(PropertiesPanel.this, "SwitchProps");
                    switchProps.associateNode(selectedNode);
                    break;
                default: // if (selectedDevice instanceof Router)
                    layout.show(PropertiesPanel.this, "RouterProps");
                    routerProps.associateNode(selectedNode);
                }
            }
        }
    }
}
