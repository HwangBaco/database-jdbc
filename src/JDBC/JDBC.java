package src.JDBC;

import javax.swing.*;
import java.sql.*;
import java.util.Set;
import javax.swing.table.DefaultTableModel;

public class JDBC {

    private Connection conn;
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

    public Connection connectJDBC() {
        try {
            conn = jdbcConnect.getConnection();
        } catch (SQLException e){
            System.out.println("데이터베이스와 연결되지 않았습니다! 변수를 확인해주세요!");
            e.printStackTrace();
        }
        return conn;
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

    public void updateEmployeeDate(Set<String> ssnList, JComboBox<String> updateItemComboBox,
                                   JTextField updateTextBox, JComboBox<String> sexComboBox, JComboBox<String> departmentComboBox) throws SQLException{
        jdbcUpdateEmployeeData.updateEmployeeDate(ssnList, updateItemComboBox, updateTextBox, sexComboBox, departmentComboBox, conn);
    }

    public void insertEmployeeData(JTextField[] fields, JComboBox<String> sexCategory) throws SQLException{
        jdbcInsertEmployeeData.insertEmployeeData(fields, sexCategory, conn);
     }

    public void deleteEmployee(Set<String> ssnList) throws SQLException{
        jdbcDeleteEmployeeData.deleteEmployee(ssnList, conn);
    }
}
