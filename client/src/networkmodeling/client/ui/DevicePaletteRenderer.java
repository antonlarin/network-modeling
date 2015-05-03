package networkmodeling.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class DevicePaletteRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list,
        Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel component = new JPanel();
        component.setAlignmentX(0.5f);
        component.setOpaque(true);

        if (isSelected) {
            component.setBackground(list.getSelectionBackground());
            component.setForeground(list.getSelectionForeground());
        } else {
            component.setBackground(list.getBackground());
            component.setForeground(list.getForeground());
        }

        JLabel icon;
        switch ((String) value) {
        case "NIC":
            icon = new JLabel(new ImageIcon(nicImage));
            break;
        case "Hub":
            icon = new JLabel(new ImageIcon(hubImage));
            break;
        case "Switch":
            icon = new JLabel(new ImageIcon(switchImage));
            break;
        default: // case "Router":
            icon = new JLabel(new ImageIcon(routerImage));
        }
        JLabel text = new JLabel((String) value);
        text.setHorizontalAlignment(JLabel.CENTER);

        component.setLayout(new BorderLayout());
        component.add(icon, BorderLayout.CENTER);
        component.add(text, BorderLayout.SOUTH);

        return component;
    }



    private static final String imageRoot = "img/";
    private static final BufferedImage nicImage;
    private static final BufferedImage hubImage;
    private static final BufferedImage switchImage;
    private static final BufferedImage routerImage;

    static {
        try {
            nicImage = ImageIO.read(new File(imageRoot + "pc.png"));
            hubImage = ImageIO.read(new File(imageRoot + "hub.png"));
            switchImage = ImageIO.read(new File(imageRoot + "switch.png"));
            routerImage = ImageIO.read(new File(imageRoot + "router.png"));
        } catch (IOException ex) {
            throw new RuntimeException("Could not load diagram icons.");
        }
    }
}
