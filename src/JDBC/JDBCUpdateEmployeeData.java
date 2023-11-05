package src.JDBC;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class JDBCUpdateEmployeeData {

    private final String baseUpdateClause = "UPDATE Employee";
    private final String baseSetClause = " SET ";
    private final String baseWhereClause = " WHERE ";
    private final HashMap<String,String> attributeMap = new HashMap<>();

    public JDBCUpdateEmployeeData(){
        attributeMap.put("Name", "Fname Minit Lname");
        attributeMap.put("Supervisor", "Super_ssn");
        attributeMap.put("Department", "Dno");
        attributeMap.put("selectDno", "SELECT Dnumber FROM Department WHERE Dname = ");
        attributeMap.put("selectSuperSsn", "SELECT Super_ssn FROM Employee WHERE Ssn = ");
    }

    private String parseCondition(String attribute, JTextField text, JComboBox<String> sex, JComboBox<String> department){
        if(attribute.equals("Dno")) return Objects.requireNonNull(department.getSelectedItem()).toString();
        if(attribute.equals("Sex")) return Objects.requireNonNull(sex.getSelectedItem()).toString();
        else return text.getText();
    }

    private String parseAttribute(String attribute){
        String tmpAttribute = attributeMap.get(attribute);
        if(tmpAttribute != null) return attributeMap.get(attribute);
        else return attribute;
    }

    private String parseName(String attribute, String condition){
        ArrayList<String> setClause = new ArrayList<>();
        String[] tmpAttribute = attribute.split(" ");
        String[] tmpCondition = Arrays.stream(condition.split(" "))
                .map(s -> "\"" + s + "\"")
                .toArray(String[]::new);

        if(tmpCondition.length != 3) return null;
        for(int i = 0; i < tmpAttribute.length; i++) {
            setClause.add(tmpAttribute[i] + " = " + tmpCondition[i]);
        }
        //System.out.println("return Value = " + String.join(", ", setClause));
        return String.join(", ", setClause);
    }

    private String findAttribute(String attribute, String condition, Connection conn) throws SQLException {
        String baseQuery = attributeMap.get(attribute) + "\"" + condition + "\"";
        Statement stmt = conn.createStatement();
        ResultSet r = stmt.executeQuery(baseQuery);
        
        if(r.next()) {
            return r.getString(1);
        }
        else return null;
    }

    private String createSql(String ssn, String setAttribute, String setCondition, Connection conn) throws SQLException {
        String updateClause = baseUpdateClause;
        String setClause = baseSetClause;
        String whereClause = baseWhereClause;

        if(setAttribute.equals("Dno")){
            String tmpDno = findAttribute("selectDno", setCondition, conn);
            if(tmpDno == null) throw new SQLDataException();
            setClause += setAttribute + " = " + tmpDno;
        } else if(setAttribute.equals(attributeMap.get("Name")) || setAttribute.equals(attributeMap.get("Supervisor"))){
            // 수정하려는 값이 Supervisor일 경우 자신의 Ssn을 이용하여 Supervisor의 Ssn값을 찾는다.
            if(setAttribute.equals(attributeMap.get("Supervisor"))){
                ssn = findAttribute("selectSuperSsn", ssn, conn);
            }
            String parsedName = parseName(attributeMap.get("Name"), setCondition);
            if(parsedName == null) throw new SQLDataException();
            setClause += parsedName;
        }else{
            setClause += setAttribute + " = " + "\"" + setCondition + "\"";
        }
        whereClause += "Ssn = " + "\"" + ssn + "\"" ;
        //System.out.println("Generated Query : " + updateClause + setClause + whereClause);
        return updateClause + setClause + whereClause;
    }

    public void updateEmployeeDate(Set<String> ssnList, JComboBox<String> category,
                                   JTextField text, JComboBox<String> sex, JComboBox<String> department, Connection conn)  throws SQLException{
        String attribute = parseAttribute(Objects.requireNonNull(category.getSelectedItem()).toString());
        String condition = parseCondition(attribute, text, sex, department);

        try (Statement stmt = conn.createStatement()) {
            for (String ssn : ssnList) {
                String sql = createSql(ssn, attribute, condition, conn);
                stmt.addBatch(sql);
            }
            stmt.executeBatch();
        } catch (SQLException sqlException){
            if(sqlException.getMessage().startsWith("Duplicate")) throw new SQLIntegrityConstraintViolationException();
            else throw sqlException;
        }
    }
}
