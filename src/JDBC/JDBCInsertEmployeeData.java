package src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class JDBCInsertEmployeeData extends Component {

    private String sql = null;

    public void insertEmployeeData(JTextField[] fields, JComboBox<String> sexCategory, Connection conn) {
        //INSERT 명령 수행
        //각 label에서 값 가져와서 sql문 작성에 활용
        String firstName = fields[0].getText(); //NOT NULL
        String middleInitial = fields[1].getText();
        String lastName = fields[2].getText(); //NOT NULL
        String ssn = fields[3].getText(); //NOT NULL
        String birthdate = fields[4].getText();
        String address = fields[5].getText();
        String sex = (String) sexCategory.getSelectedItem();
        double salary = Double.parseDouble(fields[7].getText()); //DECIMAL(10, 2)
        String super_ssn = fields[8].getText();
        int dno = (fields[9] != null && !fields[9].getText().isEmpty()) ? Integer.parseInt(fields[9].getText()) : 1;
        //dno NOT NULL DEFAULT 1 -> fields[9]에 아무것도 입력되지 않았다면 1로 설정

        try {
            sql = "INSERT INTO EMPLOYEE (fname, minit, lname, ssn, bdate, address, sex, salary, super_ssn, dno) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement p = conn.prepareStatement(sql);
            p.setString(1, firstName);
            p.setString(2, middleInitial);
            p.setString(3, lastName);
            p.setString(4, ssn);
            p.setString(5, birthdate);
            p.setString(6, address);
            p.setString(7, sex);
            p.setDouble(8, salary);
            p.setString(9, super_ssn);
            p.setInt(10, dno);

            int rowsAffected = p.executeUpdate();
            // 이 부분은 JFrame에서 사용할 수 있는 구문이므로 주석처리하였습니다.
            // 해당 함수에서 쿼리 성공 여부를 반환하고 SubFrame에서 이를 토대로 창을 띄우는 방법을 사용하는 것이 좋을 것 같습니다!!
//            if (rowsAffected > 0) {
//                JOptionPane.showMessageDialog(this, "직원 정보 추가 성공");
//            } else {
//                JOptionPane.showMessageDialog(this, "직원 정보 추가 실패");
//            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally { //명령 수행 후 DB 연결 종료
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
