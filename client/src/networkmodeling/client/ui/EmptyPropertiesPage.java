package networkmodeling.client.ui;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EmptyPropertiesPage extends JPanel {
    
    public EmptyPropertiesPage() {
        setupPage();
    }

    
    private void setupPage() {
        StringBuilder tipText = new StringBuilder();
        tipText.append("<html><body style='width:180px;color:#cccccc;");
        tipText.append("font-family:sans-serif;font-weight:bold;");
        tipText.append("font-size:24px'>");
        tipText.append("Click any device to edit its properties.");
        tipText.append("</body></html>");
        
        JLabel tip = new JLabel(tipText.toString());
        tip.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        setLayout(new BorderLayout());
        add(tip, BorderLayout.CENTER);
    }
}
