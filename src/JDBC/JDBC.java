package src.JDBC;

import javax.swing.*;
import java.sql.*;

public class JDBC {

    private Connection conn;
    private final JDBCConnect jdbcConnect;
    private final JDBCPrintReport jdbcPrintReport;
    private final JDBCInsertEmployeeData jdbcInsertEmployeeData;
    private final JDBCDeleteEmployeeData jdbcDeleteEmployeeData;

    public JDBC(String user, String password, String dbname){
        jdbcConnect = new JDBCConnect(user, password, dbname);
        jdbcPrintReport = new JDBCPrintReport();
        jdbcInsertEmployeeData = new JDBCInsertEmployeeData();
        jdbcDeleteEmployeeData = new JDBCDeleteEmployeeData();
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

    public void printReport(JCheckBox[] jCheckBoxes) {
        try {
            jdbcPrintReport.printReport(jCheckBoxes, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertEmployeeData(JTextField[] fields, JComboBox<String> sexCategory){
        return jdbcInsertEmployeeData.insertEmployeeData(fields, sexCategory, conn);
    }

    public boolean deleteEmployee(JTextField jTextField, JComboBox jComboBox1, JComboBox jComboBox2, boolean[] booleans, JComboBox jComboBox3) {
        return jdbcDeleteEmployeeData.deleteEmployee(jTextField, jComboBox1, jComboBox2, booleans, jComboBox3, conn);
    }
}