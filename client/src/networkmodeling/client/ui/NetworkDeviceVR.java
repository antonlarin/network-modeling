package networkmodeling.client.ui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import networkmodeling.core.Hub;
import networkmodeling.core.NIC;
import networkmodeling.core.NetworkDevice;
import networkmodeling.core.Switch;

public class NetworkDeviceVR {

    public NetworkDeviceVR(NetworkDevice device) {
        this.device = device;
        System.out.println(System.getProperty("user.dir"));
            try {
                if (device instanceof NIC) {
                    this.deviceIcon =
                        ImageIO.read(new File(imageRoot + "pc.png"));
                } else if (device instanceof Hub) {
                    this.deviceIcon =
                        ImageIO.read(new File(imageRoot + "hub.png"));
                } else if (device instanceof Switch) {
                    this.deviceIcon =
                        ImageIO.read(new File(imageRoot + "switch.png"));
                }
            } catch (IOException ex) {
            }
    }
    
    public Point getLocation() {
        return location;
    }
    
    public void setLocation(int x, int y) {
        location = new Point(x, y);
    }
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int halfImageWidth = deviceIcon.getWidth() / 2;
        int halfImageHeight = deviceIcon.getHeight() / 2;
        
        g2.drawImage(deviceIcon,
            location.x - halfImageWidth, location.y - halfImageHeight, null);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(location.x - halfImageWidth,
            location.y - halfImageHeight,
            2 * halfImageWidth, 2 * halfImageHeight, 5, 5);
    }
    

    private static final String imageRoot = "img/";
    
    private final NetworkDevice device;
    private Point location;
    private BufferedImage deviceIcon;
}
