package networkmodeling.client.ui;

import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;

public class WindowManager {

    public WindowManager(ClientAppModel clientAppModel) {
        this.clientAppModel = clientAppModel;
        this.mainWindow = new MainWindow(this);
    }

    public ClientAppModel getClientAppModel() {
        return clientAppModel;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void showMainWindow() {
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    public void showNICAdditionDialog(Point2D.Double newNodeLocation) {
        AddNICDialog addNicDialog =
            new AddNICDialog(clientAppModel, mainWindow, newNodeLocation);
        addNicDialog.setLocationRelativeTo(mainWindow);
        addNicDialog.setVisible(true);
    }

    public void showHubAdditionDialog(Point2D.Double newNodeLocation) {
        AddHubDialog addHubDialog =
            new AddHubDialog(clientAppModel, mainWindow, newNodeLocation);
        addHubDialog.setLocationRelativeTo(mainWindow);
        addHubDialog.setVisible(true);
    }

    public void showSwitchAdditionDialog(Point2D.Double newNodeLocation) {
        AddSwitchDialog addSwitchDialog =
            new AddSwitchDialog(clientAppModel, mainWindow, newNodeLocation);
        addSwitchDialog.setLocationRelativeTo(mainWindow);
        addSwitchDialog.setVisible(true);
    }

    public void showRouterAdditionDialog(Point2D.Double newNodeLocation) {
        AddRouterDialog addRouterDialog =
            new AddRouterDialog(this, mainWindow, newNodeLocation);
        addRouterDialog.setLocationRelativeTo(mainWindow);
        addRouterDialog.setVisible(true);
    }

    public void showNetworkTestDialog() {
        NetworkTestDialog networkTestDialog =
            new NetworkTestDialog(clientAppModel, mainWindow);
        networkTestDialog.setLocationRelativeTo(mainWindow);
        networkTestDialog.setVisible(true);
    }

    public void showRoutingTableEditDialog(boolean forExistingRouter) {
        RoutingTableDialog routingTableDialog =
            new RoutingTableDialog(clientAppModel, mainWindow,
                forExistingRouter);
        routingTableDialog.setLocationRelativeTo(mainWindow);
        routingTableDialog.setVisible(true);
    }



    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ClientAppModel clientAppModel = new ClientAppModel();
                WindowManager windowManager = new WindowManager(clientAppModel);
                windowManager.showMainWindow();
            }

        });
    }

    private final ClientAppModel clientAppModel;
    private final MainWindow mainWindow;
}
