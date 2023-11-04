package src.JDBC;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class JDBCInsertEmployeeData extends Component {

    private String sql = null;

    public void insertEmployeeData(JTextField[] fields, JComboBox<String> sexCategory, Connection conn) throws SQLException {
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
        p.executeUpdate();
    }
}
