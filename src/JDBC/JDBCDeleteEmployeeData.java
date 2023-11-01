package src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// 수민님이 작성해주시면 되는 class입니다!
public class JDBCDeleteEmployeeData extends Component {
    private String sql = null;
    private final String baseDeleteClause = "DELETE FROM EMPLOYEE WHERE ";

    public boolean deleteEmployee(JTextField text, JComboBox sex, JComboBox department, boolean[] boolArr, JComboBox where, Connection conn) {
        int idx;
        String s;
        sql = baseDeleteClause;
        if(boolArr[0]) { //sex, department 제외 다른 속성 조건설정
            String whereCondition = (String)where.getSelectedItem();
            s = text.getText();
            sql += whereCondition + "=" + s;
        } else if(boolArr[1]) { //sex 조건
            s = (String) sex.getSelectedItem();
            sql += "sex=" + s;
        } else if(boolArr[2]) { //department 조건
            idx = department.getSelectedIndex();
            if(idx==0) sql += "dno=5"; //Research 부서
            else if(idx==1) sql += "dno=4"; //Administration 부서
            else if(idx==2) sql += "dno=1"; //HQ 부서
        } else {
            JOptionPane.showMessageDialog(this, "삭제 조건을 설정하십시오");
        }

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            int res = stmt.executeUpdate(sql);

            // 이 부분은 JFrame에서 사용할 수 있는 구문이므로 주석처리하였습니다.
            // 해당 함수에서 쿼리 성공 여부를 반환하고 SubFrame에서 이를 토대로 창을 띄우는 방법을 사용하는 것이 좋을 것 같습니다!!
//            if (res > 0) {
//                JOptionPane.showMessageDialog(this, "직원 정보 추가 성공");
//            } else {
//                JOptionPane.showMessageDialog(this, "직원 정보 추가 실패");
//            }
            if(res > 0) return true;
            else return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally { //명령 수행 후 DB 연결 종료
            if (conn != null) {
                try {
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
