package src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Set;

// 수민님이 작성해주시면 되는 class입니다!
public class JDBCDeleteEmployeeData extends Component {
    private final String baseDeleteClause = "DELETE FROM EMPLOYEE WHERE ";

    public void deleteEmployee(Set<String> ssnList, Connection conn) throws SQLException{
        conn.setAutoCommit(false);
        StringBuilder sb = new StringBuilder();
        String sql = sb.append(baseDeleteClause).append("ssn = ?;").toString();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            for (String ssn : ssnList) {
                pstmt.setString(1, ssn);
                pstmt.addBatch();
                pstmt.clearParameters();
            }
            pstmt.executeBatch();
            conn.commit();
        } catch(Exception e) {
            conn.rollback();
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
