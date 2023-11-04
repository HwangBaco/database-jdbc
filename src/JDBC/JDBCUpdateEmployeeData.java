package src.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;
import java.util.Vector;

public class JDBCUpdateEmployeeData {

    private final String baseUpdateClause = "UPDATE EMPLOYEE";
    private final String baseSetClause = " SET ";
    private final String baseWhereClause = " WHERE Ssn = ";

    private String parseAttribute(){
        return null;
    }

    private String parseCondition(){
        return null;
    }

    private String findSsn(){
        return null;
    }

    private String createSql(String Ssn, String setAttribute, String setCondition) {
        String updateClause = baseUpdateClause;
        String setClause = baseSetClause;
        String whereClause = baseWhereClause;

        return updateClause + setClause + whereClause;
    }

    public void updateEmployeeDate(Set<String> ssnList, JComboBox<String> category, JTextField text, JComboBox<String> sex, JComboBox<String> department, Connection conn)  throws SQLException{
        //업데이트 버튼 누르면
        System.out.println("updateItemComboBox = " + category.getSelectedItem());
        System.out.println("updateTextBox.getText() = " + text.getText());
        System.out.println("sexComboBox = " + sex.getSelectedItem());
        System.out.println("departmentComboBox = " + department.getSelectedItem());

        System.out.println();

//        String updateQuery = createSql("", "", "");
//        String attribute = parseAttribute();
//        String condition = parseCondition();
        //PreparedStatement p = conn.prepareStatement("");

    }
}
