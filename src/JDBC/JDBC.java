package src.JDBC;

import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class JDBC {

    private Connection conn;
    private final JDBCConnect jdbcConnect;
    private final JDBCPrintReport jdbcPrintReport;
    private final JDBCInsertEmployeeData jdbcInsertEmployeeData;

    public JDBC(String user, String password, String dbname){
        jdbcConnect = new JDBCConnect(user, password, dbname);
        jdbcPrintReport = new JDBCPrintReport();
        jdbcInsertEmployeeData = new JDBCInsertEmployeeData();
    }

    public void connectJDBC() {
        try {
            conn = jdbcConnect.getConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void disconnectJDBC(){
        try{
            if(conn != null)
                conn.close();
        } catch(SQLException e){

        }
    }

    public DefaultTableModel printReport(DefaultTableModel model, JCheckBox[] jCheckBoxes) {
        try {
            model  = jdbcPrintReport.printReport(model, jCheckBoxes, conn);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public void insertEmployeeData(JTextField[] fields, JComboBox<String> sexCategory){
        jdbcInsertEmployeeData.insertEmployeeData(fields, sexCategory, conn);
    }


}