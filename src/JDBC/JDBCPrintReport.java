package src.JDBC;


import java.util.Vector;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

public class JDBCPrintReport {

    // 기본 Select, From문과 attribute 이름을 DB와 동일시하기 위해 매핑할 HashMap
    private final String baseSelectClause = "SELECT ";
    private final String baseFromClause = " FROM Employee ";
    private final HashMap<String,String> attributeMap = new HashMap<>();

    // JFrame에서의 attribute 이름과 DB에서의 attribute를 일치하기 위해 매핑 정보 추가
    // (두 환경의 attribute 이름이 다른 경우에 대해서만 추가하였음)
    public JDBCPrintReport(){
        attributeMap.put("Name", "Fname, Minit, Lname");
        attributeMap.put("Supervisor", "Super_ssn");
        attributeMap.put("Department", "dname");
        // join condition이 필요한 경우
        attributeMap.put("Join Department", "Join Department on dno = dnumber");
    }

    // checkBox에 선택된 attribute를 토대로 sql 구문을 만드는 함수
    private String createSql(JCheckBox[] checkBox){
        ArrayList<String> attribute = new ArrayList<>();
        // sql이 실제 실행될 select문, from문을 담은 변수
        String selectClause = baseSelectClause;
        String fromClause = baseFromClause;
        // JCheckBox의 모든 항목에대하여
        for(JCheckBox j : checkBox){
            // 해당 attribute가 체크되어있다면 select 절에 포함시킨다
            if(j.isSelected()) {
                // map에 매핑되어있는 정보라면 매핑되어있는 정보로 attribute 이름 추가,
                // 매핑되어 있지 않다면 JCheckBox의 이름 그대로 추가
                if(attributeMap.get(j.getText()) != null){
                    // Department가 check 되어있는 경우 department table과의 join이 필요하므로 추가
                    if(j.getText().equals("Department")){
                        fromClause += attributeMap.get("Join Department");
                    }
                    attribute.add(attributeMap.get(j.getText()));
                } else attribute.add(j.getText());
            }
        }
        selectClause += String.join(", ", attribute);
        System.out.println("Generated Query : " + selectClause + fromClause);
        return selectClause + fromClause;
    }

    // sql 쿼리를 만들어 실행하는 함수
    public DefaultTableModel printReport(DefaultTableModel model, JCheckBox[] checkBox, Connection conn) throws SQLException {
        model.setColumnCount(0);
        model.setNumRows(0);
        //String[] header = new String[];
        Vector<Object> record = new Vector<>();
        Vector<String> header = new Vector<>();

        Statement stmt = conn.createStatement();
        String sql = createSql(checkBox);

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
            record.add(false);
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    record.add(rs.getString(i));
                    System.out.printf("%s ", record.get(i));
                }
                model.addRow(record);
                System.out.println();
            }
        }
        return model;
    }

}
