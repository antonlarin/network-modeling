package networkmodeling.client.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
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
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.MouseInputAdapter;
import networkmodeling.core.modelgraph.NetworkGraphEdge;
import networkmodeling.core.modelgraph.NetworkGraphNode;
import networkmodeling.exceptions.NoFreePortsException;

public class DiagramPanel extends JPanel {

    public DiagramPanel(WindowManager windowManager) {
        this.windowManager = windowManager;
        drawnConnectionStart = null;
        drawnConnectionEnd = null;
        viewportStart = new Point(0, 0);
        viewportPanStart = null;
        panningViewport = false;
        DiagramMouseHandler mouseListener = new DiagramMouseHandler();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);

        this.setDoubleBuffered(true);
        this.setTransferHandler(new AddDeviceHandler());

        windowManager.getClientAppModel().addPropertyChangeListener(
            "visualModel", new VisualModelChangeListener());
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

            if (connection ==
                windowManager.getClientAppModel().getSelectedEdge()) {
                BasicStroke selectedConnectionStroke = new BasicStroke(3.6f);
                Color defaultColor = g2.getColor();
                g2.setColor(new Color(0, 204, 0));
                g2.setStroke(selectedConnectionStroke);
                g2.drawLine(end1Location.x, end1Location.y,
                    end2Location.x, end2Location.y);
                g2.setColor(defaultColor);
                g2.setStroke(connectionStroke);
            }

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
        case "Router":
            windowManager.showRouterAdditionDialog(diagramSpaceLocation);
            break;
        default:
            windowManager.showNICAdditionDialog(diagramSpaceLocation);
            break;
        }
    }

    private Point2D.Double convertToDiagramSpace(Point location) {
        double xScalingFactor = 1.0 / panelDiagramSpacesRatio;
        double yScalingFactor = 1.0 / panelDiagramSpacesRatio;
        double x = (location.x + viewportStart.x) * xScalingFactor;
        double y = (location.y + viewportStart.y) * yScalingFactor;
        return new Point2D.Double(x, y);
    }

    private Point convertToPanelSpace(Point2D.Double location) {
        double xScalingFactor = panelDiagramSpacesRatio;
        double yScalingFactor = panelDiagramSpacesRatio;
        int x = (int)(location.x * xScalingFactor) - viewportStart.x;
        int y = (int)(location.y * yScalingFactor) - viewportStart.y;
        return new Point(x, y);
    }

    private BufferedImage getDeviceIcon(NetworkGraphNode deviceNode) {
        switch (deviceNode.getNodeDevice().getType()) {
        case NIC:
            return nicImage;
        case Hub:
            return hubImage;
        case Switch:
            return switchImage;
        default: // case Router:
            return routerImage;
        }
    }

    private Rectangle getDeviceNodeRectangle(NetworkGraphNode deviceNode) {
        Point location = convertToPanelSpace(
            new Point2D.Double(deviceNode.getX(), deviceNode.getY()));
        BufferedImage deviceIcon;
        switch (deviceNode.getNodeDevice().getType()) {
        case NIC:
            deviceIcon = nicImage;
            break;
        case Hub:
            deviceIcon = hubImage;
            break;
        case Switch:
            deviceIcon = switchImage;
            break;
        default: // case Router:
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

    private boolean pointOverEdge(NetworkGraphEdge connectionEdge,
        Point point) {
        NetworkGraphNode edgeEnd1Node = connectionEdge.getFirstNode();
        NetworkGraphNode edgeEnd2Node = connectionEdge.getSecondNode();
        Point edgeEnd1 = convertToPanelSpace(
            new Point2D.Double(edgeEnd1Node.getX(), edgeEnd1Node.getY()));
        Point edgeEnd2 = convertToPanelSpace(
            new Point2D.Double(edgeEnd2Node.getX(), edgeEnd2Node.getY()));

        double width = Math.abs(edgeEnd1.x - edgeEnd2.x);
        double height = Math.abs(edgeEnd1.y - edgeEnd2.y);
        double offset = 3.5;
        if (width > height) {
            double leftX;
            double leftY;
            double rightY;
            if (edgeEnd1.x < edgeEnd2.x) {
                leftX = edgeEnd1.x;
                leftY = edgeEnd1.y;
                rightY = edgeEnd2.y;
            } else {
                leftX = edgeEnd2.x;
                leftY = edgeEnd2.y;
                rightY = edgeEnd1.y;
            }
            double alpha = (point.x - leftX) / width;
            if (alpha < 0 || 1 < alpha) {
                return false;
            } else {
                double tangent = height / width;
                double offsetMultiplier = Math.sqrt(1 + tangent * tangent);
                return Math.abs(point.y - leftY - alpha * (rightY - leftY)) <=
                    offset * offsetMultiplier;
            }
        } else {
            double topX;
            double bottomX;
            double topY;
            if (edgeEnd1.y < edgeEnd2.y) {
                topY = edgeEnd1.y;
                topX = edgeEnd1.x;
                bottomX = edgeEnd2.x;
            } else {
                topY = edgeEnd2.y;
                topX = edgeEnd2.x;
                bottomX = edgeEnd1.x;
            }
            double alpha = (point.y - topY) / height;
            if (alpha < 0 || 1 < alpha) {
                return false;
            } else {
                double tangent = width / height;
                double offsetMultiplier = Math.sqrt(1 + tangent * tangent);
                return Math.abs(point.x - topX - alpha * (bottomX - topX)) <=
                    offset * offsetMultiplier;
            }
        }
    }



    private static final int panelDiagramSpacesRatio = 500;
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
    private Point oldViewportStart;
    private final Point viewportStart;
    private Point viewportPanStart;
    private boolean panningViewport;
    private boolean creatingConnection;
    private NetworkGraphNode drawnConnectionStart;
    private Point drawnConnectionEnd;



    private class DiagramMouseHandler extends MouseInputAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                boolean pressedOnDevice = false;
                LinkedList<NetworkGraphNode> deviceNodes =
                    windowManager.getClientAppModel().getVisualModel().
                    GetGraph().getNodes();
                for (NetworkGraphNode deviceNode : deviceNodes) {
                    if (pointOverNode(deviceNode, e.getPoint())) {
                        if (e.isControlDown()) {
                            pressedOnDevice = true;
                            creatingConnection = true;
                            drawnConnectionStart = deviceNode;
                            drawnConnectionEnd = e.getPoint();
                            windowManager.getClientAppModel().
                                setSelectedNode(null);
                        } else {
                            pressedOnDevice = true;
                            windowManager.getClientAppModel().setSelectedNode(
                                deviceNode);
                        }
                        windowManager.getClientAppModel().setSelectedEdge(null);
                        break;
                    }
                }

                if (!pressedOnDevice) {
                    windowManager.getClientAppModel().setSelectedNode(null);

                    boolean pressedOnConnection = false;
                    LinkedList<NetworkGraphEdge> connectionEdges =
                        windowManager.getClientAppModel().getVisualModel().
                        GetGraph().getEdges();
                    for (NetworkGraphEdge connectionEdge : connectionEdges) {
                        if (pointOverEdge(connectionEdge, e.getPoint())) {
                            windowManager.getClientAppModel().setSelectedEdge(
                                connectionEdge);
                            pressedOnConnection = true;
                            break;
                        }
                    }

                    if (!pressedOnConnection) {
                        windowManager.getClientAppModel().setSelectedEdge(null);
                    }
                }
            } else if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                windowManager.getClientAppModel().setSelectedNode(null);
                windowManager.getClientAppModel().setSelectedEdge(null);
                panningViewport = true;
                oldViewportStart = new Point(viewportStart.x, viewportStart.y);
                viewportPanStart = new Point();
                viewportPanStart.x = e.getPoint().x;
                viewportPanStart.y = e.getPoint().y;
                DiagramPanel.this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
            DiagramPanel.this.repaint();
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
                        try {
                            windowManager.getClientAppModel().
                                connectDevices(
                                    drawnConnectionStart, deviceNode);
                        } catch (NoFreePortsException ex) {
                            JOptionPane.showMessageDialog(
                                windowManager.getMainWindow(),
                                ex.getDescription());
                        }
                        break;
                    }
                }
                creatingConnection = false;
                drawnConnectionStart = null;
                drawnConnectionEnd = null;
                DiagramPanel.this.repaint();
            } else if (panningViewport) {
                panningViewport = false;
                oldViewportStart = null;
                viewportPanStart = null;
                DiagramPanel.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (windowManager.getClientAppModel().getSelectedNode() != null) {
                Point2D.Double diagramLocation =
                    convertToDiagramSpace(e.getPoint());
                windowManager.getClientAppModel().changeSelectedNodeLocation(
                    diagramLocation);
            } else if (creatingConnection) {
                drawnConnectionEnd = e.getPoint();
            } else if (panningViewport) {
                viewportStart.x = oldViewportStart.x - e.getX() +
                    viewportPanStart.x;
                viewportStart.y = oldViewportStart.y - e.getY() +
                    viewportPanStart.y;
            }
            DiagramPanel.this.repaint();
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
