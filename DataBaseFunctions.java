/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.system;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mahesh
 */
public class DataBaseFunctions {
    
    static String userName;
    static String password;
    static Connection con = null;
    
    public DataBaseFunctions(){
        
    }
    public DataBaseFunctions(String name,String pswd){
        userName = name;
        password = pswd;
        
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",userName,password);
        }catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null,con);
                String message[] = e.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }
    }
    
    public static void createTable(String createQuery,String tableName){
        try{
                //Connection con=connectDB();
                int n = createQuery.length();
                char[] query = createQuery.toCharArray();
                query[n-1] = ')';
                createQuery = String.valueOf(query);
            
                String create = "create table ";
                create+=tableName;
                create+="(";
                create+=createQuery;

                System.out.println(create);
                Statement createStmt=con.createStatement();
                createStmt.executeUpdate(create);
                JOptionPane.showMessageDialog(null, "Table Created Successfully !!!");    
                createQuery = "";
            }catch(SQLException e1){
                String message[] = e1.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
            }
    }
    public static int addRows(String tableName,String[] columnNames,int ColumnSize,DefaultTableModel model){
        int ColumnNo = -1;
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            String sql;
            sql = "Select * from ";
            sql+=tableName;
            try (ResultSet rs = stmt.executeQuery(sql)) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Object[] obj = new Object[ColumnSize];
                while(rs.next()){
                    for(int i=0;i<ColumnSize;i++){
                        if(i!=ColumnSize){
                            if(rsmd.getColumnTypeName(i+1).equalsIgnoreCase("DATE")){
                                String []str = rs.getString(columnNames[i]).split(" ");
                                obj[i] = str[0];
                                ColumnNo=i;
                            }else{
                                obj[i] = rs.getString(columnNames[i]);
                            }
                        }
                    }
                    model.addRow(obj);
                }
            }
            stmt.close();
            return ColumnNo;
        }catch(SQLException se){
                String message[] = se.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }catch(Exception e){
                String message[] = e.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
        }
        return -1;
    }
     public static void deleteRow(String tableName,String[] columnName,String[] values,int size,int ColumnNo){
        String query = "delete from ";
        query +=tableName;
        query += " where ";
        for(int i=0;i<size;i++){
            if(ColumnNo!=i){
                query += columnName[i];
                query +="=";
                query +="?";
                if(i == size-1){
                }else{
                    query +=" and ";
                }
            }
        }
        System.out.println(query);
        try{
            PreparedStatement stmt;
            stmt = con.prepareStatement(query);
            for(int i=1;i<=values.length;i++){
                stmt.setString(i, values[i-1]);
            }
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Row Deleted Successfully !!!");
        }catch(SQLException e){
                String message[] = e.getMessage().split(":");
                if(message.length==1)
                    JOptionPane.showMessageDialog(null, message[0]);
                else
                    JOptionPane.showMessageDialog(null, message[1]);
        }
    }
     
     public static void displayTableData(DefaultTableModel model,String tableName){
        Statement stmt = null;
        String columnName;
        columnName = " ";
        String dataType = " ";
        int length;
        String constraint = "None";
        try{
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql;
            sql = "describe ";
            sql+=tableName;
            DatabaseMetaData dbm = con.getMetaData();
            try (ResultSet rs1 = dbm.getColumns(null, "%", tableName, "%")) {
                ResultSet rs2 = dbm.getPrimaryKeys(null, null, tableName);
                Vector<String> primaryKey = new Vector<String>();
                while(rs2.next()){
                    primaryKey.add( rs2.getString("COLUMN_NAME"));
                }
                while(rs1.next()){
                    columnName = rs1.getString("COLUMN_NAME");
                    dataType = rs1.getString("TYPE_NAME");
                    length = rs1.getInt("COLUMN_SIZE");
                    for (String primaryKey1 : primaryKey) {
                        if (primaryKey1.compareTo(columnName) == 0) {
                            constraint = "Primary Key";
                        }
                    }
                    model.addRow(new Object[]{columnName,dataType,length,constraint});
                    constraint = "None";
                }
            }
            stmt.close();
        }catch(SQLException se){
                String message[] = se.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }catch(Exception e){
                String message[] = e.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
        }
    }
     
     public static String[] getColumnNames(String tableName){   
        String query = "Select * from "+tableName;
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);   
            ResultSetMetaData column = rs.getMetaData();
            String [] columnNames = new String[column.getColumnCount()];
            int i = 1;
            int j= 0;
            while(j< column.getColumnCount()){
                columnNames[j] = column.getColumnName(i);
                i++;
                j++;
            }
            return columnNames;
        }catch(Exception e){
                String message[] = e.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
            return null;
        }
    }
    public static String[] getTableNames(){
        try{
        Statement stmt = con.createStatement();
        Statement stmt2 = con.createStatement();
        System.out.println(userName);
        ResultSet noOfColumn = stmt.executeQuery("select count(table_name)as si from all_tables where owner = '"+userName.toUpperCase()+"'");
        ResultSet tables = stmt2.executeQuery("select table_name from all_tables where owner = '"+userName.toUpperCase()+"'");
        int count;
        noOfColumn.next();
        count = noOfColumn.getInt("SI");
        count+=1;
        String [] table_names = new String[count];
        table_names[0]="Select";
        int i=1;
        while(tables.next()){
            table_names[i]=tables.getString("TABLE_NAME");
            i++;
        } 
        return table_names;
        }catch(Exception e){
                String message[] = e.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
            return null;
        }
    }
    
    public static void dropTable(String tableName,JComboBox jComboBox1){
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            
            String sql;
            sql = "drop table ";
            sql+=tableName;
            stmt.executeQuery(sql);
            jComboBox1.removeItemAt(jComboBox1.getSelectedIndex());
            stmt.close();
            JOptionPane.showMessageDialog(null,"Table Droped Successfully !!!");
        }catch(SQLException | HeadlessException se){
                String message[] = se.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
        }
    }
    
    public static int insertRow(String table_name,JTextField jTextField[],int colCount){
        try{
            String sql="insert into ";
            sql+=table_name;
            sql+=" values(";
            for(int i=0;i<colCount;i++)
                sql+="?,";
            int n = sql.length();
            char[] query = sql.toCharArray();
            query[n-1] = ')';
            sql = String.valueOf(query);
            System.out.println(sql);
            PreparedStatement ps=con.prepareStatement(sql);
            for(int i=1;i<=colCount;i++)
                ps.setString(i,jTextField[i-1].getText());
            
            ResultSet rs= ps.executeQuery();
            return 1;
        }catch(Exception e){
                String message[] = e.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
            return 0;
        }
    }
    public static void removeColumn(String tableName,String columnName){
        String sql = "alter table ";
        sql+=tableName;
        sql+=" drop column ";
        sql+=columnName;
        try{
            System.out.println(sql);
            Statement createStmt=con.createStatement();
            createStmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null,"Column Deleted Successfully !!!");
        }catch(SQLException e1){
                String message[] = e1.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }
    }
     public static void updateRow(String tableName,String[] columnName,String[] oldValues,String[] newValues,int size){
        String query = "update ";
        query +=tableName;
        query += " set ";
        for(int i=0;i<size;i++){
            query += columnName[i];
            query +="=";
            query +="?";
            if(i == size-1){
            }else{
                query +=" , ";
            }
        }
        query += " where ";
        for(int i=0;i<size;i++){
            query += columnName[i];
            query +="=";
            query +="?";
            if(i == size-1){
            }else{
                query +=" and ";
            }
        }
        System.out.println(query);
        try{
            PreparedStatement stmt;
            stmt = con.prepareStatement(query);
            for(int i=1;i<=size;i++){
                stmt.setString(i, newValues[i-1]);
            }
            for(int i=size+1,j=0;j<size;i++,j++){
                stmt.setString(i, oldValues[j]);
            }
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null,"Row Updated Successfully !!!");
        }catch(SQLException e){
                String message[] = e.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }
    }
     
    public static void renameColumn(String tableName,String oldColumn,String newName){
        String sql = "alter table ";
        sql+=tableName;
        sql+=" rename column ";
        sql+=oldColumn;
        sql+=" to ";
        sql+=newName;
        try{
            System.out.println(sql);
            Statement createStmt;
            createStmt = con.createStatement();
            createStmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null,"Column Renamed Successfully !!!");
        }catch(SQLException e1){
                String message[] = e1.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }
    }
    
    public static void modifyColumn(String tableName,String oldConstriant,JTextField name,JComboBox dataType,JTextField length,JComboBox constraint){
        String sql = "alter table ";
        sql+=tableName;
        sql+=" modify ";
        sql+=name.getText();
        sql+=" "+dataType.getSelectedItem().toString();
        sql+="("+length.getText()+")";
        if(constraint.getSelectedItem().toString().compareTo(oldConstriant)!=0){
            if(!"None".equals(constraint.getSelectedItem().toString())){
                sql+=constraint.getSelectedItem().toString();
            }
        }
        try{
            System.out.println(sql);
            Statement createStmt=con.createStatement();
            createStmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null,"Column Modifyed Successfully !!!");
        }catch(SQLException e1){
                String message[] = e1.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }
    }
    
    public static void addColumn(String tableName,JTextField name,JComboBox dataType,JTextField length,JComboBox constraint){
        String sql = "alter table ";
        sql+=tableName;
        sql+=" add ";
        sql+=name.getText();
        if(dataType.getSelectedItem().toString().equalsIgnoreCase("Date"))
            sql+=" "+dataType.getSelectedItem().toString();
        else{
            sql+=" "+dataType.getSelectedItem().toString();
            sql+="("+length.getText()+")";
        }
        if(!"None".equals(constraint.getSelectedItem().toString())){
            sql+=" "+constraint.getSelectedItem().toString();
        }
        try{
            System.out.println(sql);
            Statement createStmt=con.createStatement();
            createStmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null,"Column Added Successfully !!!");
        }catch(SQLException e1){
                String message[] = e1.getMessage().split(":"); 
                JOptionPane.showMessageDialog(null, message[1].toUpperCase());
        }
    }
}
