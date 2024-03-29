
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author uxer
 */
public class mainpage extends javax.swing.JFrame {

    /**
     * Creates new form mainpage
     */
    public mainpage() {
        initComponents(); 
        this.setLocationRelativeTo(null);
        refreshThread.start();
        checkLowQty.start();
    }
    
    public mainpage(String fname) {
        initComponents();
        jLabel1.setText("Welcome "+fname);
        this.setLocationRelativeTo(null);
        refreshThread.start();
        checkLowQty.start();
    }

    product product_obj = new product();
    conn con = new conn();
    
    Object id = null;
    
    void clearAddProductFields(){
        pntf.setText(null);
        pqty.setValue(0);
        ppr.setText(null);
        pntf.requestFocus();
        xst_qty.setText(null);
    }
    
    final void refresh(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(con.url,con.username,con.password);
            
            String sql = "select * from products;";
            Statement stmt = (Statement) conn.createStatement();
            
            ResultSet rs = stmt.executeQuery(sql);
            DefaultTableModel model = (DefaultTableModel) ptable.getModel();
            model.setRowCount(0);
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("id"),rs.getString("product_name"),rs.getString("quantity"),rs.getString("price")});
            }
              
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(mainpage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(mainpage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    Thread refreshThread = new Thread(new Runnable() {     
        @Override
        public void run(){
            try{
                while(true){
                    refresh();
                    //System.out.println("Refresh");
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(mainpage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    });
    
    final void checkLowQuantity(){
        Notification n = new Notification();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(con.url,con.username,con.password);
            
            String sql = "select * from products;";
            String status_sql = "UPDATE products SET status=? WHERE id=?;";
            Statement stmt = (Statement) conn.createStatement();
            
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(status_sql);
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                String id = rs.getString("id");
                int qty = Integer.parseInt(rs.getString("quantity"));
                int status = rs.getInt("status");
                String product = rs.getString("product_name");
                
                if (qty < 5 && status != 1){
                    pstmt.setInt(1, 1);
                    pstmt.setString(2, id);
                    pstmt.executeUpdate();
                    n.displayNotification(product);
                }else if(qty > 5 && status == 1){
                    
                    pstmt.setInt(1, 2);
                    pstmt.setString(2, id);
                    pstmt.executeUpdate();
                
                }
            }
                
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(mainpage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(mainpage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(mainpage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    Thread checkLowQty = new Thread(new Runnable(){
        
        @Override
        public void run(){
            try{
                while(true){
                    checkLowQuantity();
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(mainpage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
    
    final void search(String keyword){
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection(con.url,con.username,con.password);
        
            String sql = "SELECT * FROM products WHERE id LIKE ? OR product_name LIKE ?";
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
            
            pstmt.setString(1, "%"+keyword+"%");
            pstmt.setString(2, "%"+keyword+"%");
            
            ResultSet rs = pstmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) ptable.getModel();
            model.setRowCount(0);
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("id"),rs.getString("product_name"),rs.getString("quantity"),rs.getString("price")});
            }     
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    final void enableAddProductFields(){
        pntf.setEnabled(true);
        pqty.setEnabled(true);
        ppr.setEnabled(true);
        clearAddProductFields();
    }
    
    class Notification{
    
        void displayNotification(String product) throws AWTException{
        
        SystemTray tray = SystemTray.getSystemTray();
        
        Image image = Toolkit.getDefaultToolkit().createImage("img/warning.png");
        
        TrayIcon trayIcon = new TrayIcon(image,"Tray Icon"); 
        
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
        
        trayIcon.displayMessage("LOW QUANTITY", product+" product low on quantity", TrayIcon.MessageType.WARNING);
        
        }
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addproductframe = new javax.swing.JFrame();
        pntf = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ppr = new javax.swing.JFormattedTextField();
        pqty = new javax.swing.JSpinner();
        add_btn = new javax.swing.JButton();
        save_btn = new javax.swing.JButton();
        xst_qty = new javax.swing.JLabel();
        addqty_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ptable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        search_tf = new javax.swing.JTextField();
        search_btn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        addproductframe.setMinimumSize(new java.awt.Dimension(400, 300));

        jLabel2.setText("Product Name:");

        jLabel3.setText("Quantity:");

        jLabel4.setText("Price:");

        ppr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        ppr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pprFocusLost(evt);
            }
        });
        ppr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pprKeyReleased(evt);
            }
        });

        pqty.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        add_btn.setText("Add");
        add_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_btnActionPerformed(evt);
            }
        });

        save_btn.setText("Save");
        save_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_btnActionPerformed(evt);
            }
        });

        addqty_btn.setText("Add Quantity");
        addqty_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addqty_btnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addproductframeLayout = new javax.swing.GroupLayout(addproductframe.getContentPane());
        addproductframe.getContentPane().setLayout(addproductframeLayout);
        addproductframeLayout.setHorizontalGroup(
            addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addproductframeLayout.createSequentialGroup()
                .addGroup(addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addproductframeLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(addproductframeLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xst_qty, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pqty, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addproductframeLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pntf, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addproductframeLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ppr, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(addproductframeLayout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addGroup(addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(save_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(add_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(addqty_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        addproductframeLayout.setVerticalGroup(
            addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addproductframeLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pntf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(xst_qty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(pqty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addproductframeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ppr))
                .addGap(64, 64, 64)
                .addComponent(addqty_btn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(add_btn)
                .addGap(3, 3, 3)
                .addComponent(save_btn)
                .addGap(30, 30, 30))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "product", "qty", "price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ptable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(ptable);

        jButton1.setText("Add Product");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        search_tf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search_tfKeyReleased(evt);
            }
        });

        search_btn.setText("Search");
        search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_btnActionPerformed(evt);
            }
        });

        jButton2.setText("Add Quantity");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(search_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(search_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(search_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(search_btn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(94, 94, 94)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pprKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pprKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_pprKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here
        addproductframe.setVisible(true);
        addproductframe.setLocationRelativeTo(rootPane);
        addproductframe.setAlwaysOnTop(true);
        
        add_btn.setVisible(true);
        save_btn.setVisible(false);
        addqty_btn.setVisible(false);
        
        this.enableAddProductFields();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void pprFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pprFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_pprFocusLost

    private void add_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_btnActionPerformed
        // TODO add your handling code here:
        String pn = pntf.getText();
        int qty = (int) pqty.getValue();
        Object pr = ppr.getValue();
        
        int r = product_obj.addProduct(pn, qty, pr);
        if(r==1){
            JOptionPane.showMessageDialog(addproductframe, "New Product Successfully");
            clearAddProductFields();
            refresh();
        }
    }//GEN-LAST:event_add_btnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int r = ptable.getSelectedRow();
        if(r==-1){
            JOptionPane.showMessageDialog(rootPane, "Please Select a product", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            id = ptable.getValueAt(r, 0);
            Object product_name = ptable.getValueAt(r, 1);
            int c = JOptionPane.showConfirmDialog(rootPane, "This will delete "+product_name+"?\nClick OK to continue","Confirm Delete",JOptionPane.OK_CANCEL_OPTION);
            
            if(c==JOptionPane.YES_OPTION){
                int cc = JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to delete "+product_name+"?","Delete",JOptionPane.YES_NO_OPTION);
                if(cc==JOptionPane.YES_OPTION){
                    int re = product_obj.deleteProduct(id);
                    if(re==1){
                        JOptionPane.showMessageDialog(rootPane, product_name+" Deleted from database");
                        refresh();
                    }
                }
            }
            
            //System.out.println(c);
        }
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        int table_row = ptable.getSelectedRow();
        this.enableAddProductFields();
        
        if(table_row != -1){
        id = ptable.getValueAt(table_row, 0);
        Object product_name = ptable.getValueAt(table_row, 1);
        Object pro_qty = ptable.getValueAt(table_row, 2);
        Object pro_price = ptable.getValueAt(table_row, 3);
        
        pntf.setText((String) product_name);
        pqty.setValue(Integer.valueOf((String) pro_qty));
        ppr.setValue(Double.valueOf((String) pro_price));
        
        addproductframe.setVisible(true);
        addproductframe.setLocationRelativeTo(rootPane);
        addproductframe.setAlwaysOnTop(true);
        save_btn.setVisible(true);
        add_btn.setVisible(false);
        addqty_btn.setVisible(false);
        
        pqty.setEnabled(false);
        }else{
            JOptionPane.showMessageDialog(rootPane, "Please Select a product", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void save_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_btnActionPerformed
        // TODO add your handling code here:
        String newpn = pntf.getText();
        Object newpr = ppr.getValue();
        
        int r = product_obj.editProduct(id, newpn, newpr);
        if(r==1){
            JOptionPane.showMessageDialog(addproductframe, "Product Edit Successfully");
            addproductframe.setVisible(false);
            this.refresh();
        }else{
            JOptionPane.showMessageDialog(addproductframe, "Problem Editing Produc", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_save_btnActionPerformed

    private void search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_btnActionPerformed
        // TODO add your handling code here:
        String keyword = search_tf.getText();
        this.search(keyword);
    }//GEN-LAST:event_search_btnActionPerformed

    private void search_tfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search_tfKeyReleased
        // TODO add your handling code here:
        String keyword = search_tf.getText();
        this.search(keyword);
    }//GEN-LAST:event_search_tfKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int row = ptable.getSelectedRow();
        if(row != -1){
            addproductframe.setVisible(true);
            addproductframe.setLocationRelativeTo(this);
            addproductframe.setAlwaysOnTop(true);
            save_btn.setVisible(false);
            add_btn.setVisible(false);
            addqty_btn.setVisible(true);
            
            id = ptable.getValueAt(row, 0);
            Object pn = ptable.getValueAt(row, 1);
            Object qty = ptable.getValueAt(row, 2);
            Object pr = ptable.getValueAt(row, 3);
            
            pntf.setEnabled(false);
            ppr.setEnabled(false);
            pqty.setEnabled(true);
            
            pntf.setText(pn.toString());
            xst_qty.setText(qty.toString());
            ppr.setValue(Double.valueOf(pr.toString()));
            pqty.setValue(0);
        }else{
            JOptionPane.showMessageDialog(rootPane, "Please Select a product", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void addqty_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addqty_btnActionPerformed
        // TODO add your handling code here:
        String pn = pntf.getText();
        Object qty = pqty.getValue();
        int c = JOptionPane.showConfirmDialog(addproductframe, "Would you like to add\n "+qty+"\n to "+pn+" product?", "Add Quantity", JOptionPane.YES_NO_OPTION);
        if(c == JOptionPane.YES_OPTION){
            int r = product_obj.addQuantity(id, qty);
            if(r==1){
                JOptionPane.showMessageDialog(addproductframe, "Quantity Updated");
                addproductframe.setVisible(false);
                this.refresh();
            }
        }   
    }//GEN-LAST:event_addqty_btnActionPerformed

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
            java.util.logging.Logger.getLogger(mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainpage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            
            public void run() {
                new mainpage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_btn;
    private javax.swing.JFrame addproductframe;
    private javax.swing.JButton addqty_btn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField pntf;
    private javax.swing.JFormattedTextField ppr;
    private javax.swing.JSpinner pqty;
    private javax.swing.JTable ptable;
    private javax.swing.JButton save_btn;
    private javax.swing.JButton search_btn;
    private javax.swing.JTextField search_tf;
    private javax.swing.JLabel xst_qty;
    // End of variables declaration//GEN-END:variables
}
