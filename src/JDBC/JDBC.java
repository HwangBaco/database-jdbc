package src.JDBC;

import javax.swing.*;
import java.sql.*;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class JDBC {

    private static Connection conn;
    private final JDBCConnect jdbcConnect;
    private final JDBCRetrieveEmployeeData jdbcPrintReport;
    private final JDBCInsertEmployeeData jdbcInsertEmployeeData;
    private final JDBCDeleteEmployeeData jdbcDeleteEmployeeData;
    private final JDBCUpdateEmployeeData jdbcUpdateEmployeeData;

    public JDBC(String user, String password, String dbname){
        jdbcConnect = new JDBCConnect(user, password, dbname);
        jdbcPrintReport = new JDBCRetrieveEmployeeData();
        jdbcInsertEmployeeData = new JDBCInsertEmployeeData();
        jdbcDeleteEmployeeData = new JDBCDeleteEmployeeData();
        jdbcUpdateEmployeeData = new JDBCUpdateEmployeeData();
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
        } catch(SQLException ignored){

        }
    }

    public DefaultTableModel printReport(DefaultTableModel model, JCheckBox[] checkBox,
                                         JComboBox<String> category, JTextField text, JComboBox<String> sex, JComboBox<String> department) {

        try {
            model  = jdbcPrintReport.printReport(model, checkBox, category, text, sex, department, conn);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public void updateEmployeeDate(){
        //jdbcUpdateEmployeeData.updateEmployeeDate();
    }

    public void insertEmployeeData(JTextField[] fields, JComboBox<String> sexCategory) throws SQLException{
        jdbcInsertEmployeeData.insertEmployeeData(fields, sexCategory, conn);
     }

    public void deleteEmployee(Set<String> ssnList) throws SQLException{
        try {
            jdbcDeleteEmployeeData.deleteEmployee(ssnList, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
