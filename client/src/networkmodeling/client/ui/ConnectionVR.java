package networkmodeling.client.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

public class ConnectionVR {
    
    public ConnectionVR(NetworkDeviceVR end1, NetworkDeviceVR end2) {
        this.end1 = end1;
        this.end2 = end2;
        color = randomColor();
    }
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        Point connectionEnd1 = end1.getLocation();
        Point connectionEnd2 = end2.getLocation();
        Color oldColor = g2.getColor();
        g2.setColor(color);
        g2.drawLine(connectionEnd1.x, connectionEnd1.y,
            connectionEnd2.x, connectionEnd2.y);
        g2.setColor(oldColor);
    }

    
    private Color randomColor() {
        Random generator = new Random();
        float r = generator.nextFloat() * 0.65f;
        float g = generator.nextFloat() * 0.65f;
        float b = generator.nextFloat() * 0.65f;
        
        return new Color(r, g, b);
    }
    
    private NetworkDeviceVR end1;
    private NetworkDeviceVR end2;
    private Color color;
}
