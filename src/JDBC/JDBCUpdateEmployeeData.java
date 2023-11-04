package src.JDBC;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

public class JDBCUpdateEmployeeData {

    private final String baseUpdateClause = "UPDATE EMPLOYEE";
    private final String baseSetClause = " SET ";
    private final String baseWhereClause = " WHERE ";

    private String parseAttribute(){
        return null;
    }

    private String parseCondition(){
        return null;
    }

    private String createSql(String setAttribute, String setCondition, String whereCondition){
        String updateClause = baseUpdateClause;
        String setClause = baseSetClause;
        String whereClause = baseWhereClause;

        return updateClause + setClause + whereClause;
    }

    public void updateEmployeeDate(JComboBox<String> category, JTextField text, JComboBox<String> sex, JComboBox<String> department, String ssn, Connection conn){
        String updateQuery = createSql("", "", "");
        String attribute = parseAttribute();
        String condition = parseCondition();
        //PreparedStatement p = conn.prepareStatement("");

    }
}
