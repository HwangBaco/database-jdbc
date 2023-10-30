package src;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.sql.*;

class SubFrame extends JFrame implements ActionListener {
    private final String[] labels = {"First Name","Middle Initial","Last Name","Ssn","Birthdate","Address", "Sex", "Salary","Super_ssn", "Dno"};
    private JTextField[] fields = new JTextField[10];
    private final String[] sex = {"M", "F"};

    JButton addBtn;
    JComboBox<String> sexCategory;

    Connection conn;
    Statement stmt;
    ResultSet rs;
    String sql = null;

    public SubFrame() {
        super("직원 등록");
        setSize(500, 500);
        setLocation(350, 50);

        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("새로운 직원 정보 추가");
        JLabel empty = new JLabel(" ");
        Font font = new Font("SansSerif", Font.BOLD, 20);
        empty.setFont(font);
        title.setFont(font);
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        titlePanel.add(empty);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        sexCategory = new JComboBox<String>(sex);
        JPanel attributePanel = new JPanel(new GridLayout(11,2,5,10));
        for(int i=0;i<10;i++) {
            attributePanel.add(new JLabel(labels[i]));
            if(labels[i] == "Sex"){
                attributePanel.add(sexCategory);
            }else {
                fields[i] = new JTextField(25);
                attributePanel.add(fields[i]);
            }
        }
        attributePanel.add(new JLabel(" "));

        JPanel btnPanel = new JPanel();
        addBtn = new JButton("정보 추가하기");
        addBtn.addActionListener(this);
        btnPanel.add(addBtn);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel totalPanel = new JPanel();
        totalPanel.add(titlePanel);
        totalPanel.add(attributePanel);
        totalPanel.add(addBtn);
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        add("North", totalPanel);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //디비에 값 넣기
        //pk에 널값이 들어오면 오류 띄우기
        if (e.getSource() == addBtn) {
            accDb();
            insertEmployeeData();
        }
        dispose();
    }

    private void accDb() {
        String url = "jdbc:mysql://localhost:3306/company_forhw?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
        String acct = "root";
        String passwrd = "newabt12!";
        try {
            conn = DriverManager.getConnection(url, acct, passwrd);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private void insertEmployeeData() {
        String firstName = fields[0].getText();
        String middleInitial = fields[1].getText();
        String lastName = fields[2].getText();
        String ssn = fields[3].getText();
        String birthdate = fields[4].getText();
        String address = fields[5].getText();
        String sex = (String) sexCategory.getSelectedItem();
        double salary = Double.parseDouble(fields[7].getText());
        String super_ssn = fields[8].getText();
        int dno = Integer.parseInt(fields[9].getText()); // Assuming dno is an integer

        try {
            // Define the SQL INSERT statement using a prepared statement
            String insertQuery = "INSERT INTO EMPLOYEE (fname, minit, lname, ssn, bdate, address, sex, salary, super_ssn, dno) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, middleInitial);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, ssn);
            preparedStatement.setString(5, birthdate);
            preparedStatement.setString(6, address);
            preparedStatement.setString(7, sex);
            preparedStatement.setDouble(8, salary);
            preparedStatement.setString(9, super_ssn);
            preparedStatement.setInt(10, dno);

            // Execute the INSERT statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Insert was successful
                JOptionPane.showMessageDialog(this, "Employee information added successfully");
            } else {
                // Insert failed
                JOptionPane.showMessageDialog(this, "Failed to add employee information");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Handle the exception
                }
            }
        }
    }
}
