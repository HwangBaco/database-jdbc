package src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

// 수민님이 작성해주시면 되는 class입니다!
public class JDBCDeleteEmployeeData extends Component {
    private String sql = null;
    private final String baseDeleteClause = "DELETE FROM EMPLOYEE WHERE ";

    public void deleteEmployee(JTextField text, JComboBox<String> sex, JComboBox<String> department, boolean[] boolArr, JComboBox<String> where, Connection conn) throws SQLException{
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

        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
    }
}
