package networkmodeling.server;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class serverUIMainWindow extends javax.swing.JFrame {

    /**
     * Creates new form serverUIMainWindow
     */
    public serverUIMainWindow(ServerModel _model) {
        initComponents();
        
        model = _model;
        isServerStarted = false;
        serverLog = new String();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        serverLogTextPane = new javax.swing.JTextPane();
        startStopButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Networkmodeling Server");

        jLabel1.setText("Лог сервера:");

        serverLogTextPane.setEditable(false);
        jScrollPane1.setViewportView(serverLogTextPane);

        startStopButton.setText("Старт");
        startStopButton.setName("startStopButton"); // NOI18N
        startStopButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startStopButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startStopButton))
                .addGap(0, 146, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(startStopButton)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        startStopButton.getAccessibleContext().setAccessibleName("startStopButton");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startStopButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startStopButtonMouseClicked
        if(isServerStarted)
        {
            isServerStarted = false;
            model.stopServer();
            startStopButton.setText("Старт");
        }
        else{
            isServerStarted = true;
            try {
                model.startServer();
            } catch (UnknownHostException ex) {
                Logger.getLogger(serverUIMainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            startStopButton.setText("Стоп");
        }
        
        serverLogTextPane.setText(model.getServerLog());
    }//GEN-LAST:event_startStopButtonMouseClicked

    private boolean isServerStarted;
    private String serverLog;
    private ServerModel model;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane serverLogTextPane;
    private javax.swing.JButton startStopButton;
    // End of variables declaration//GEN-END:variables
}
