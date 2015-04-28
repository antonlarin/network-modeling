package networkmodeling.client.ui;

import java.awt.CardLayout;
import javax.swing.JPanel;
import networkmodeling.client.Client;

public class PropertiesPanel extends JPanel {
    
    public PropertiesPanel(Client client) {
        this.client = client;
        nicProps = new NICPropertiesPage();
        hubProps = new HubPropertiesPage();
        switchProps = new SwitchPropertiesPage();
        
        setLayout(new CardLayout());
        
        add(nicProps, "NicProps");
        add(hubProps, "HubProps");
        add(switchProps, "SwitchProps");
    }
    
    private final Client client;
    private final NICPropertiesPage nicProps;
    private final HubPropertiesPage hubProps;
    private final SwitchPropertiesPage switchProps;
}
