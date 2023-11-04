package src.JDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
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

    public void updateEmployeeDate(DefaultTableModel model, JComboBox<String> category, JTextField text, JComboBox<String> sex, JComboBox<String> department, Connection conn)  throws SQLException{
        //업데이트 버튼 누르면
        System.out.println("updateItemComboBox = " + category.getSelectedItem());
        System.out.println("updateTextBox.getText() = " + text.getText());
        System.out.println("sexComboBox = " + sex.getSelectedItem());
        System.out.println("departmentComboBox = " + department.getSelectedItem());

        Vector<Vector> dataVector = model.getDataVector();
        int columnCount = model.getColumnCount();
        for(int i = 0; i < columnCount; i++){
            System.out.print(model.getColumnName(i) + " ");
        }
        System.out.println();
        for(Vector v : dataVector){
            for(int i = 1; i < v.size(); i++){
                if((boolean)v.get(0)){
                    if(v.get(i) != null) System.out.print(v.get(i).toString() + " ");
                }
            }
            System.out.println();
        }
//        String updateQuery = createSql("", "", "");
//        String attribute = parseAttribute();
//        String condition = parseCondition();
        //PreparedStatement p = conn.prepareStatement("");

    }
}
