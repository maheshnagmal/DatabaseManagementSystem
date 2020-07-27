/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.system;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Mahesh
 */
public class updateTableData extends javax.swing.JFrame {

    /**
     * Creates new form updateTableData
     */
    
    JButton add,modify,rename,cancel;
    int size;
    JTextField name=null;   
    JTextField length=null;
    JComboBox dataType=null;
    JComboBox constraint=null;
    JLabel[] jLabel1=null;
    String[] columnNames ={};
    String[] values = {};
    String tableName = "";
    String [] str = {};
    String []newValues = {};
    
    public updateTableData(String tableName,String columnNames[],String[] values){
       
        String[] data = {"None" ,"CHAR","VARCHAR","VARCHAR2","NUMBER","DATE" };
        dataType = new JComboBox(data);
        String[] constriant = {"None","Primary Key","Not Null"};
        constraint = new JComboBox(constriant);
        
        constraint.setSelectedItem(values[1]);
        
        name = new JTextField(30);
        length = new JTextField(30);
        
        size = columnNames.length;
        str = new String[size];
        jLabel1 = new JLabel[size];
        add = new JButton("Add");
        modify = new JButton("Modify");
        rename = new JButton("Rename");
        cancel = new JButton("Cancel");
        
        for(int i=0,y=50;i<size;i++){
            jLabel1[i] = new JLabel();
            jLabel1[i].setText(columnNames[i]);
            jLabel1[i].setBounds(30, y, 80, 40);
            add(jLabel1[i]);
            y+=80;
        }
        name.setBounds(110, 50, 150, 40);
        dataType.setBounds(110, 130, 150, 40);
        length.setBounds(110,210,150,40);
        constraint.setBounds(110,290,150,40);
        add(name);
        add(dataType);
        add(length);
        add(constraint);
        
        name.setText(values[0]);
        dataType.setSelectedItem(values[1]);
        length.setText(values[2]);
        constraint.setSelectedItem(values[3]);
        
        
        setLayout(null);
        setTitle("View Rows");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataBaseFunctions.addColumn(tableName,name,dataType,length,constraint);
                dataType.setSelectedIndex(0);
                constraint.setSelectedIndex(0);
                length.setText("");
                name.setText("");
            }
        });
        
        rename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataBaseFunctions.renameColumn(tableName,values[0],name.getText());
                JOptionPane.showMessageDialog(null,"Column Renamed Successfully !!!");
                dataType.setSelectedIndex(0);
                constraint.setSelectedIndex(0);
                length.setText("");
                name.setText("");
            }
        });
        
        modify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataBaseFunctions.modifyColumn(tableName,values[3],name,dataType,length,constraint);
                JOptionPane.showMessageDialog(null,"Column Modifyed Successfully !!!");
                dataType.setSelectedIndex(0);
                constraint.setSelectedIndex(0);
                length.setText("");
                name.setText("");
            }
        });
        
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataType.setSelectedIndex(0);
                constraint.setSelectedIndex(0);
                length.setText("");
                name.setText("");
                dispose();
            }
        });
        
        add.setBounds(25,480,130,40);
        modify.setBounds(165,480,130,40);
        rename.setBounds(305,480,130,40);
        cancel.setBounds(445,480,130,40);
        add(add);
        add(modify);
        add(rename);
        add(cancel);
       
        
        setPreferredSize(new Dimension(600,600));
        pack();
        setLocationRelativeTo(null);
    }
    public updateTableData() {
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(updateTableData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(updateTableData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(updateTableData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(updateTableData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new updateTableData().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
