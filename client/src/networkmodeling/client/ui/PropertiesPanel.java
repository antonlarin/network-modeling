package networkmodeling.client.ui;

import java.awt.CardLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import networkmodeling.client.Client;

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
}
