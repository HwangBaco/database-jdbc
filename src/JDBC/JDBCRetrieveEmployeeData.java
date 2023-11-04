package src.JDBC;


import java.util.Objects;
import java.util.Vector;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

public class JDBCRetrieveEmployeeData {

    // 기본 Select, From문과 attribute 이름을 DB와 동일시하기 위해 매핑할 HashMap
    private final String baseSelectClause = "SELECT ";
    private final String baseFromClause = " FROM Employee ";
    private final String baseWhereCluase = " WHERE ";
    private final HashMap<String,String> attributeMap = new HashMap<>();

    // JFrame에서의 attribute 이름과 DB에서의 attribute를 일치하기 위해 매핑 정보 추가
    // (두 환경의 attribute 이름이 다른 경우에 대해서만 추가하였음)
    public JDBCRetrieveEmployeeData(){
        attributeMap.put("Supervisor", "Super_ssn");
        attributeMap.put("Department", "Dname");
        // join condition이 필요한 경우
        attributeMap.put("Dname", "Join Department on Dno = Dnumber");
        attributeMap.put("total", "");
    }

    // attribute가 Dname, 혹은 Sex인 경우 해당 변수에 맞는 값에서 가져오고 아니라면 text에 있는 내용을 가져옴
    private String parseCondition(String attribute, JTextField text, JComboBox<String> sex, JComboBox<String> department){
        if(attribute.equals("Dname")) return Objects.requireNonNull(department.getSelectedItem()).toString();
        if(attribute.equals("Sex")) return Objects.requireNonNull(sex.getSelectedItem()).toString();
        else return text.getText();
    }

    // attributeMap에 value가 존재한다면 해당 value 반환, 아니라면 매개변수 자체가 attribute 이름이 되므로 반환
    private String parseAttribute(String attribute){
        if(attributeMap.get(attribute) != null){
            return attributeMap.get(attribute);
        } else{
            return attribute;
        }
    }

    // checkBox에 선택된 attribute를 토대로 sql 구문을 만드는 함수
    private String createSql(JCheckBox[] checkBox, String attribute, String condition){
        // select문에 추가될 attribute들을 추가할 list
        ArrayList<String> attributes = new ArrayList<>();
        // sql이 실제 실행될 select문, from문을 담은 변수, whereClause의 경우 조건이 있는 경우에만 where 절을 포함한다.
        String selectClause = baseSelectClause;
        String fromClause = baseFromClause;
        String whereClause = "";

        for(JCheckBox j : checkBox){
            // 해당 attribute가 체크되어있다면 select 절에 포함시킨다
            if(j.isSelected()) {
                // 매핑 정보 임시 저장
                String tmpAttribute = attributeMap.get(j.getText());
                // 매핑 정보가 존재하면 매핑되어있는 정보로 attribute 이름 추가,
                // 매핑되어 있지 않다면 JCheckBox의 text 그대로 추가
                if(tmpAttribute != null){
                    // 매핑된 정보가 dname인 경우 department table과의 join이 필요하므로 추가
                    if(tmpAttribute.equals("Dname")){
                        fromClause += attributeMap.get(tmpAttribute);
                    }
                    attributes.add(tmpAttribute);
                } else attributes.add(j.getText());
            }
        }
        // 선택된 attributes 들을 join하여 select 절에 추가
        selectClause += String.join(", ", attributes);

        // parameter 값을 토대로 where절 완성
        if(!attribute.isBlank() && !condition.isBlank()){
            if(attribute.equals("Salary")) whereClause = baseWhereCluase + attribute + " >= " + "\"" + condition + "\"";
            else whereClause = baseWhereCluase + attribute + " = " + "\"" + condition + "\"";
        }
        System.out.println("Generated Query : " + selectClause + fromClause + whereClause);
        return selectClause + fromClause + whereClause;
    }

    // sql 쿼리를 만들어 실행하는 함수
    public DefaultTableModel printReport(DefaultTableModel model, JCheckBox[] checkBox,
                                         JComboBox<String> category, JTextField text, JComboBox<String> sex, JComboBox<String> department, Connection conn) throws SQLException {
        model.setColumnCount(0);
        model.setNumRows(0);
        //String[] record = new String[100];
        Vector<String> header = new Vector<>();

        // 조건을 parsing하고 sql에 생성 함수의 매개변수로 넘긴다.
        Statement stmt = conn.createStatement();
        String attribute = parseAttribute(Objects.requireNonNull(category.getSelectedItem()).toString());
        String condition = parseCondition(attribute, text, sex, department);
        String sql = createSql(checkBox, attribute, condition);

        // sql 쿼리를 실제로 실행
        try(ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            model.setColumnCount(columnCount);
            System.out.printf("%d%n", model.getColumnCount());

            // attribute 이름을 전부 가져오는 구문
            header.add("선택");
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i); // 열의 이름 가져오기
                System.out.printf("%s ", columnName);
                header.add(columnName);
            }
            model.setColumnIdentifiers(header);
            System.out.println();

            // sql의 결과를 모두 출력하는 구문
            while (rs.next()) {
                Vector<Object> record = new Vector<>();
                record.add(false);
                for (int i = 1; i <= columnCount; i++) {
                    record.add(rs.getString(i));
                    System.out.printf("%s ", record.get(i));
                }
                model.addRow(record);
                System.out.println();
            }
        } catch (SQLException e){
            System.out.println("쿼리문 오류");
        }
        return model;
    }

}
