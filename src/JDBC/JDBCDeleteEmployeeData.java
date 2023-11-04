package src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Set;

public class JDBCDeleteEmployeeData extends Component {
    private StringBuilder sb;
    private final String baseDeleteClause = "DELETE FROM EMPLOYEE WHERE ";

    public void deleteEmployee(Set<String> ssnList, Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        for (String ssn : ssnList) {
            sb = new StringBuilder();
            String sql = sb.append(baseDeleteClause).append("ssn = ").append(ssn).append(";").toString();
            stmt.executeUpdate(sql);
        }
    }
}
