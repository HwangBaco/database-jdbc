package src.JDBC;

import java.awt.*;
import java.sql.*;
import java.util.Set;

// 수민님이 작성해주시면 되는 class입니다!
public class JDBCDeleteEmployeeData extends Component {
    private final String baseDeleteClause = "DELETE FROM EMPLOYEE WHERE ";

    public void deleteEmployee(Set<String> ssnList, Connection conn) throws SQLException{
        conn.setAutoCommit(false);
        PreparedStatement pstmt = getQueryForm(conn);
        try {
            setQueryForm(ssnList, pstmt);
            pstmt.executeBatch();
            conn.commit();
        } catch(Exception e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            pstmt.close();
        }
    }

    private PreparedStatement getQueryForm(Connection conn) throws SQLException {
        PreparedStatement pstmt;
        StringBuilder sb = new StringBuilder();
        String sql = sb.append(baseDeleteClause).append("ssn = ?;").toString();
        pstmt = conn.prepareStatement(sql);
        return pstmt;
    }

    private static void setQueryForm(Set<String> ssnList, PreparedStatement pstmt) throws SQLException {
        for (String ssn : ssnList) {
            pstmt.setString(1, ssn);
            pstmt.addBatch();
            pstmt.clearParameters();
        }
    }
}
