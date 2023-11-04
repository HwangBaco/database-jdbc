package src;

import src.JDBC.JDBC;

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

    // SubFrame에서 JDBC 클래스를 사용하기 위한 변수 생성 및 인스턴스 생성
    // 사용자에 따라 id, password 변경
    private static final String dbacct = "root";
    private static final String passwrd = "newabt12!";
    private static final String dbname = "company_forhw";
    JDBC jdbc = new JDBC(dbacct, passwrd, dbname);

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
                //제약조건 만족시 jdbc 연결 후 INSERT 명령 수행 후 연결해제
                jdbc.connectJDBC();
                if(jdbc.insertEmployeeData(fields, sexCategory))
                    JOptionPane.showMessageDialog(this, "직원 정보 추가 성공");
                else JOptionPane.showMessageDialog(this, "직원 정보 추가 실패");
            }
        }
        dispose();
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

}
