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
        if (e.getSource() == addBtn) {
            //"정보 추가하기" 버튼 클릭시 수행
            if (!checkNotNullFields()) {
                //fname, lname, ssn에 아무것도 입력되지 않았다면 명령 거부(NOT NULL)
                JOptionPane.showMessageDialog(this, "Fname, Lname, Ssn에는 값이 들어가야 합니다.");
            } else if(!isValidDate()) {
                //Bdate가 yyyy-mm-dd 형식이 아니라면 명령 거부(DATE 형 속성)
                JOptionPane.showMessageDialog(this, "생일에는 yyyy-mm-dd 형식이 들어가야 합니다.");
            } else {
                //제약조건 만족시 INSERT 명령 수행
                accDb();
                insertEmployeeData();
                dispose();
            }
        }
    }

    private boolean checkNotNullFields() {
        //NOT NULL 제약이 걸려있는 부분 검사
        for (int i = 0; i < 10; i++) {
            if(i==0 || i==2 || i==3) {
                if (fields[i] == null || fields[i].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidDate() {
        //Bdate에 yyyy-mm-dd 형식이 올바르게 들어갔는지 검사
        //null 허용이므로 null이라면 true 반환
        if(fields[4]==null || fields[4].getText().isEmpty()) return true;

        String date = fields[4].getText();
        char[] dateArr = date.toCharArray();

        if(date.length() != 10) return false;
        if(dateArr[4] != '-' || dateArr[7] != '-') return false;
        for(int i=0; i<10; i++) {
            if(i==4 || i==7) continue;
            if(!Character.isDigit(dateArr[i])) return false;
        }
        return true;
    }

    private void accDb() {
        //db 연결 부분
        String url = "jdbc:mysql://localhost:3306/company_forhw?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
        String acct = "root";
        String passwrd = "";
        //url, acct, passwrd는 본인 환경대로 수정
        try {
            conn = DriverManager.getConnection(url, acct, passwrd);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private void insertEmployeeData() {
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

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "직원 정보 추가 성공");
            } else {
                JOptionPane.showMessageDialog(this, "직원 정보 추가 실패");
            }
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
