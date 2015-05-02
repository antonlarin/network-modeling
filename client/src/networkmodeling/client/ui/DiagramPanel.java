package networkmodeling.client.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;
import networkmodeling.core.Hub;
import networkmodeling.core.NIC;
import networkmodeling.core.Switch;
import networkmodeling.core.modelgraph.NetworkGraphEdge;
import networkmodeling.core.modelgraph.NetworkGraphNode;

public class DiagramPanel extends JPanel implements Observer {
    
    public DiagramPanel(WindowManager windowManager) {
        this.windowManager = windowManager;
        windowManager.getClientAppModel().getClientDaemon().addObserver(this);
        drawnConnectionStart = null;
        drawnConnectionEnd = null;
        viewportStart = new Point(0, 0);
        DiagramMouseHandler mouseListener = new DiagramMouseHandler();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

        this.setDoubleBuffered(true);
        this.setTransferHandler(new AddDeviceHandler());
        
        windowManager.getClientAppModel().addPropertyChangeListener(
            "visualModel", new VisualModelChangeListener());
    }



    @Override
    public void update(Observable o, Object arg) {
        repaint();
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
        Graphics2D g2 = (Graphics2D) g;
        Stroke defaultStroke = g2.getStroke();
        BasicStroke connectionStroke = new BasicStroke(2f);
        g2.setStroke(connectionStroke);

        LinkedList<NetworkGraphEdge> connections =
            windowManager.getClientAppModel().getVisualModel().
            GetGraph().getEdges();
        for (NetworkGraphEdge connection : connections) {
            NetworkGraphNode end1 = connection.getFirstNode();
            NetworkGraphNode end2 = connection.getSecondNode();
            Point end1Location = convertToPanelSpace(
                new Point2D.Double(end1.getX(), end1.getY()));
            Point end2Location = convertToPanelSpace(
                new Point2D.Double(end2.getX(), end2.getY()));
            g2.drawLine(end1Location.x, end1Location.y,
                end2Location.x, end2Location.y);
        }

        if (creatingConnection) {
            Point start = convertToPanelSpace(
                new Point2D.Double(drawnConnectionStart.getX(),
                    drawnConnectionStart.getY()));
            Point end = drawnConnectionEnd;
            g.drawLine(start.x, start.y, end.x, end.y);
        }

        g2.setStroke(defaultStroke);
    }

    private void drawDevices(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        LinkedList<NetworkGraphNode> deviceNodes =
            windowManager.getClientAppModel().getVisualModel().
            GetGraph().getNodes();
        for (NetworkGraphNode deviceNode : deviceNodes) {
            BufferedImage deviceIcon = getDeviceIcon(deviceNode);
            int halfImageWidth = deviceIcon.getWidth() / 2;
            int halfImageHeight = deviceIcon.getHeight() / 2;
            
            Point location = convertToPanelSpace(
                new Point2D.Double(deviceNode.getX(), deviceNode.getY()));
            g2.drawImage(deviceIcon, location.x - halfImageWidth,
                location.y - halfImageHeight, null);
            
            if (deviceNode ==
                windowManager.getClientAppModel().getSelectedNode()) {
                Stroke defaultStroke = g2.getStroke();
                Color defaultColor = g2.getColor();
                BasicStroke selectionStroke = new BasicStroke(1.3f);
                g2.setStroke(selectionStroke);
                g2.setColor(new Color(0, 204, 0));
                
                Rectangle nodeRect = getDeviceNodeRectangle(deviceNode);
                g2.drawRoundRect(nodeRect.x, nodeRect.y,
                    nodeRect.width, nodeRect.height, 8, 8);
                
                g2.setColor(defaultColor);
                g2.setStroke(defaultStroke);
            }
        }
    }

    private void addDevice(String deviceTypeName, Point location) {
        Point2D.Double diagramSpaceLocation = convertToDiagramSpace(location);
        switch (deviceTypeName) {
        case "Hub":
            windowManager.showHubAdditionDialog(diagramSpaceLocation);
            break;
        case "Switch":
            windowManager.showSwitchAdditionDialog(diagramSpaceLocation);
            break;
//        case "Router":
//            windowManager.showRouterAdditionDialog(diagramSpaceLocation);
//            break;
        default:
            windowManager.showNICAdditionDialog(diagramSpaceLocation);
            break;
        }
    }

    private Point2D.Double convertToDiagramSpace(Point location) {
        double xScalingFactor = 1.0 / this.getWidth();
        double yScalingFactor = 1.0 / this.getHeight();
        double x = (location.x - viewportStart.x) * xScalingFactor;
        double y = (location.y - viewportStart.y) * yScalingFactor;
        return new Point2D.Double(x, y);
    }

    private Point convertToPanelSpace(Point2D.Double location) {
        double xScalingFactor = this.getWidth();
        double yScalingFactor = this.getHeight();
        int x = viewportStart.x + (int)(location.x * xScalingFactor);
        int y = viewportStart.y + (int)(location.y * yScalingFactor);
        return new Point(x, y);
    }
    
    private BufferedImage getDeviceIcon(NetworkGraphNode deviceNode) {
        if (deviceNode.getNodeDevice() instanceof NIC) {
            return nicImage;
        } else if (deviceNode.getNodeDevice() instanceof Hub) {
            return hubImage;
        } else if (deviceNode.getNodeDevice() instanceof Switch) {
            return switchImage;
        } else { // if (deviceNode.getNodeDevice() instanceof Router)
            return routerImage;
        }
    }
    
    private Rectangle getDeviceNodeRectangle(NetworkGraphNode deviceNode) {
        Point location = convertToPanelSpace(
            new Point2D.Double(deviceNode.getX(), deviceNode.getY()));
        BufferedImage deviceIcon;
        if (deviceNode.getNodeDevice() instanceof NIC) {
            deviceIcon = nicImage;
        } else if (deviceNode.getNodeDevice() instanceof Hub) {
            deviceIcon = hubImage;
        } else if (deviceNode.getNodeDevice() instanceof Switch) {
            deviceIcon = switchImage;
        } else { // if (deviceNode.getNodeDevice() instanceof Router)
            deviceIcon = routerImage;
        }
        int fullWidth = deviceIcon.getWidth();
        int fullHeight = deviceIcon.getHeight();
        int halfWidth = fullWidth / 2;
        int halfHeight= fullHeight / 2;
        int offset = 2;
        
        return new Rectangle(
            location.x - halfWidth - offset, location.y - halfHeight - offset,
            fullWidth + 2 * offset, fullHeight + 2 * offset);
    }
    
    private boolean pointOverNode(NetworkGraphNode deviceNode, Point point) {
        return getDeviceNodeRectangle(deviceNode).contains(point);
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

    private final WindowManager windowManager;
    private final Point viewportStart;
    private boolean creatingConnection;
    private NetworkGraphNode drawnConnectionStart;
    private Point drawnConnectionEnd;



    private class DiagramMouseHandler extends MouseInputAdapter {
        
        @Override
        public void mousePressed(MouseEvent e) {
            boolean pressedOnDevice = false;
            LinkedList<NetworkGraphNode> deviceNodes =
                windowManager.getClientAppModel().getVisualModel().
                GetGraph().getNodes();
            for (NetworkGraphNode deviceNode : deviceNodes) {
                if (pointOverNode(deviceNode, e.getPoint()) &&
                    e.getButton() == BUTTON1) {
                    if (e.isControlDown()) {
                        pressedOnDevice = true;
                        creatingConnection = true;
                        drawnConnectionStart = deviceNode;
                        drawnConnectionEnd = e.getPoint();
                        windowManager.getClientAppModel().setSelectedNode(null);
                    } else {
                        pressedOnDevice = true;
                        windowManager.getClientAppModel().setSelectedNode(
                            deviceNode);
                    }
                    break;
                }
            }
            
            if (!pressedOnDevice) {
                windowManager.getClientAppModel().setSelectedNode(null);
            }
            repaint();
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            if (creatingConnection) {
                LinkedList<NetworkGraphNode> deviceNodes =
                windowManager.getClientAppModel().getVisualModel().
                    GetGraph().getNodes();
                for (NetworkGraphNode deviceNode : deviceNodes) {
                    if (deviceNode != drawnConnectionStart &&
                        pointOverNode(deviceNode, e.getPoint())) {
                        windowManager.getClientAppModel().
                            connectDevices(drawnConnectionStart, deviceNode);
                        break;
                    } else {
                        repaint();
                    }
                }
                creatingConnection = false;
                drawnConnectionStart = null;
                drawnConnectionEnd = null;
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (windowManager.getClientAppModel().getSelectedNode() != null) {
                Point2D.Double diagramLocation =
                    convertToDiagramSpace(e.getPoint());
                windowManager.getClientAppModel().changeSelectedNodeLocation(
                    diagramLocation);
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
    
    private class VisualModelChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            DiagramPanel.this.repaint();
        }
    }
}
