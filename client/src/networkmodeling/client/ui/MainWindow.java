/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkmodeling.client.ui;

/**
 *
 * @author antonlarin
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hubParametersPanel1 = new networkmodeling.client.ui.HubParametersPanel();
        nICParametersPanel1 = new networkmodeling.client.ui.NICParametersPanel();
        toolbarPanel = new javax.swing.JPanel();
        paletteScrollPane = new javax.swing.JScrollPane();
        paletteDeviceList = new javax.swing.JList();
        deviceParametersPanel = new javax.swing.JPanel();
        nicParametersContainer = new javax.swing.JPanel();
        nicParametersPanel = new networkmodeling.client.ui.NICParametersPanel();
        hubParametersContainer = new javax.swing.JPanel();
        hubParametersPanel2 = new networkmodeling.client.ui.HubParametersPanel();
        switchParametersContainer = new javax.swing.JPanel();
        switchParametersPanel1 = new networkmodeling.client.ui.SwitchParametersPanel();
        diagramPanel1 = new networkmodeling.client.ui.DiagramPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        toolbarPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout toolbarPanelLayout = new javax.swing.GroupLayout(toolbarPanel);
        toolbarPanel.setLayout(toolbarPanelLayout);
        toolbarPanelLayout.setHorizontalGroup(
            toolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        toolbarPanelLayout.setVerticalGroup(
            toolbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );

        paletteScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Device palette"));

        paletteDeviceList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "NIC", "Hub", "Switch", "Router" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        paletteDeviceList.setDragEnabled(true);
        paletteScrollPane.setViewportView(paletteDeviceList);

        deviceParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Device  parameters"));
        deviceParametersPanel.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout nicParametersContainerLayout = new javax.swing.GroupLayout(nicParametersContainer);
        nicParametersContainer.setLayout(nicParametersContainerLayout);
        nicParametersContainerLayout.setHorizontalGroup(
            nicParametersContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nicParametersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
        );
        nicParametersContainerLayout.setVerticalGroup(
            nicParametersContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nicParametersContainerLayout.createSequentialGroup()
                .addComponent(nicParametersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 242, Short.MAX_VALUE))
        );

        deviceParametersPanel.add(nicParametersContainer, "NicCard");

        javax.swing.GroupLayout hubParametersContainerLayout = new javax.swing.GroupLayout(hubParametersContainer);
        hubParametersContainer.setLayout(hubParametersContainerLayout);
        hubParametersContainerLayout.setHorizontalGroup(
            hubParametersContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hubParametersPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
        );
        hubParametersContainerLayout.setVerticalGroup(
            hubParametersContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hubParametersContainerLayout.createSequentialGroup()
                .addComponent(hubParametersPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 304, Short.MAX_VALUE))
        );

        deviceParametersPanel.add(hubParametersContainer, "HubCard");

        javax.swing.GroupLayout switchParametersContainerLayout = new javax.swing.GroupLayout(switchParametersContainer);
        switchParametersContainer.setLayout(switchParametersContainerLayout);
        switchParametersContainerLayout.setHorizontalGroup(
            switchParametersContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(switchParametersPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
        );
        switchParametersContainerLayout.setVerticalGroup(
            switchParametersContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchParametersContainerLayout.createSequentialGroup()
                .addComponent(switchParametersPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 304, Short.MAX_VALUE))
        );

        deviceParametersPanel.add(switchParametersContainer, "SwitchCard");

        javax.swing.GroupLayout diagramPanel1Layout = new javax.swing.GroupLayout(diagramPanel1);
        diagramPanel1.setLayout(diagramPanel1Layout);
        diagramPanel1Layout.setHorizontalGroup(
            diagramPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
        );
        diagramPanel1Layout.setVerticalGroup(
            diagramPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolbarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(deviceParametersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paletteScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diagramPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(paletteScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deviceParametersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(diagramPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel deviceParametersPanel;
    private networkmodeling.client.ui.DiagramPanel diagramPanel1;
    private javax.swing.JPanel hubParametersContainer;
    private networkmodeling.client.ui.HubParametersPanel hubParametersPanel1;
    private networkmodeling.client.ui.HubParametersPanel hubParametersPanel2;
    private networkmodeling.client.ui.NICParametersPanel nICParametersPanel1;
    private javax.swing.JPanel nicParametersContainer;
    private networkmodeling.client.ui.NICParametersPanel nicParametersPanel;
    private javax.swing.JList paletteDeviceList;
    private javax.swing.JScrollPane paletteScrollPane;
    private javax.swing.JPanel switchParametersContainer;
    private networkmodeling.client.ui.SwitchParametersPanel switchParametersPanel1;
    private javax.swing.JPanel toolbarPanel;
    // End of variables declaration//GEN-END:variables
}
