package networkmodeling.client.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;
import networkmodeling.core.Hub;
import networkmodeling.core.IpAddress;
import networkmodeling.core.MacAddress;
import networkmodeling.core.NIC;
import networkmodeling.core.NetworkDevice;
import networkmodeling.core.Switch;

public class DiagramPanel extends JPanel {
    
    public DiagramPanel() {
        connections = new LinkedList<>();
        devices = new LinkedList<>();
        selectedDevice = null;
        drawnConnectionStart = null;
        drawnConnectionEnd = null;
        DiagramMouseHandler mouseListener = new DiagramMouseHandler();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        
        // Throwaway code!
//        short o = (short) 1;
//        NIC nic = new NIC(
//            new MacAddress(o, o, o, o, o, o),
//            new IpAddress(o, o, o, o), new IpAddress(o, o, o, o));
//
//        Switch sw = new Switch(new MacAddress(o, o, o, o, o, o), 8);
//        NetworkDeviceVR nicvr = new NetworkDeviceVR(nic);
//        nicvr.setLocation(100, 100);
//        NetworkDeviceVR swvr = new NetworkDeviceVR(sw);
//        swvr.setLocation(200, 250);
//        ConnectionVR conn = new ConnectionVR(nicvr, swvr);
//        devices.push(nicvr);
//        devices.push(swvr);
//        connections.push(conn);
        // Throwaway code!
        
        this.setTransferHandler(new AddDeviceHandler());
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
        
        if (creatingConnection) {
            Graphics2D g2 = (Graphics2D) g;
            Point start = drawnConnectionStart.getLocation();
            Point end = drawnConnectionEnd;
            g.drawLine(start.x, start.y, end.x, end.y);
        }
    }

    private void drawDevices(Graphics g) {
        for (NetworkDeviceVR device : devices) {
            device.draw(g);
        }
    }
    
    private class DiagramMouseHandler extends MouseInputAdapter {
        
        @Override
        public void mousePressed(MouseEvent e) {
            boolean pressedOnDevice = false;
            for (NetworkDeviceVR device : devices) {
                if (device.hasOnIt(e.getPoint()) && e.getButton() == BUTTON1) {
                    if (e.isControlDown()) {
                        pressedOnDevice = true;
                        creatingConnection = true;
                        drawnConnectionStart = device;
                        drawnConnectionEnd = e.getPoint();
                        selectedDevice = null;
                    } else {
                        pressedOnDevice = true;
                        selectedDevice = device;
                    }
                    break;
                }
            }
            
            if (!pressedOnDevice) {
                selectedDevice = null;
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (creatingConnection) {
                for (NetworkDeviceVR device : devices) {
                    if (device != drawnConnectionStart &&
                        device.hasOnIt(e.getPoint())) {
                        ConnectionVR newConnection =
                            new ConnectionVR(drawnConnectionStart, device);
                        connections.add(newConnection);
                        break;
                    }
                }
                creatingConnection = false;
                drawnConnectionStart = null;
                drawnConnectionEnd = null;
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedDevice != null) {
                selectedDevice.setLocation(e.getX(), e.getY());
                DiagramPanel.this.repaint();
            } else if (creatingConnection) {
                drawnConnectionEnd = e.getPoint();
                DiagramPanel.this.repaint();
            }
        }
    }
    
    private class AddDeviceHandler extends TransferHandler {
        
        @Override
        public boolean canImport(TransferSupport support) {
            if (!support.isDrop()) {
                return false;
            }
            
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }
        
        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }
            
            Transferable transferable = support.getTransferable();
            String deviceType;
            try {
                deviceType = (String) transferable.getTransferData(
                        DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException ex) {
                return false;
            }

            DropLocation location = support.getDropLocation();
            DiagramPanel.this.addDevice(deviceType, location.getDropPoint());

            return true;
        }
    }

    private void addDevice(String deviceType, Point location) {
        NetworkDeviceVR newDevice;
        NetworkDevice underlyingDevice;
        if (deviceType.equals("Hub")) {
            underlyingDevice = new Hub();
        } else if (deviceType.equals("Switch")) {
            underlyingDevice = new Switch();
        } else {
            underlyingDevice = new NIC();
        }
        newDevice = new NetworkDeviceVR(underlyingDevice);
        newDevice.setLocation(location);
        devices.add(newDevice);
        DiagramPanel.this.repaint();
    }
    
    private LinkedList<ConnectionVR> connections;
    private LinkedList<NetworkDeviceVR> devices;
    private NetworkDeviceVR selectedDevice;
    private boolean creatingConnection;
    private NetworkDeviceVR drawnConnectionStart;
    private Point drawnConnectionEnd;
}
