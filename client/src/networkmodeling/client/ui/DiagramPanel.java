package networkmodeling.client.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.LinkedList;
import javax.swing.JPanel;
import networkmodeling.core.IpAddress;
import networkmodeling.core.MacAddress;
import networkmodeling.core.NIC;
import networkmodeling.core.Switch;

public class DiagramPanel extends JPanel {
    
    public DiagramPanel() {
        connections = new LinkedList<>();
        devices = new LinkedList<>();
        // Throwaway code!
        short o = (short) 1;
        NIC nic = new NIC(
            new MacAddress(o, o, o, o, o, o),
            new IpAddress(o, o, o, o), new IpAddress(o, o, o, o));

        Switch sw = new Switch(new MacAddress(o, o, o, o, o, o), 8);
        NetworkDeviceVR nicvr = new NetworkDeviceVR(nic);
        nicvr.setLocation(100, 100);
        NetworkDeviceVR swvr = new NetworkDeviceVR(sw);
        swvr.setLocation(200, 250);
        ConnectionVR conn = new ConnectionVR(nicvr, swvr);
        devices.push(nicvr);
        devices.push(swvr);
        connections.push(conn);
        // Throwaway code!
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        fillBackgroundWithWhite(g);
        drawConnections(g);
        drawDevices(g);
    }

    
    private void fillBackgroundWithWhite(Graphics g) {
        Rectangle bounds = g.getClipBounds();
        Color oldColor = g.getColor();
        g.setColor(Color.white);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(oldColor);
    }

    private void drawConnections(Graphics g) {
        for (ConnectionVR connection : connections) {
            connection.draw(g);
        }
    }

    private void drawDevices(Graphics g) {
        for (NetworkDeviceVR device : devices) {
            device.draw(g);
        }
    }
    
    private LinkedList<ConnectionVR> connections;
    private LinkedList<NetworkDeviceVR> devices;
}
