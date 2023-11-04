package src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Set;

// 수민님이 작성해주시면 되는 class입니다!
public class JDBCDeleteEmployeeData extends Component {
    private StringBuilder sb;
    private final String baseDeleteClause = "DELETE FROM EMPLOYEE WHERE ";

    public void deleteEmployee(Set<String> ssnList, Connection conn) throws SQLException{
        try {
            Statement stmt = conn.createStatement();
            for (String ssn : ssnList) {
                sb = new StringBuilder();
                String sql = sb.append(baseDeleteClause).append("ssn = ").append(ssn).append(";").toString();
                System.out.println("sql = " + sql);
                stmt.execute(sql);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
