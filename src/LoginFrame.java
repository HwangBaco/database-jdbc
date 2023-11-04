package src;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import src.JDBC.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;

public class LoginFrame extends JFrame implements ActionListener {

    // db connection param
    JButton loginBtn;
    JTextField id;
    JTextField pw;
    JTextField dbName;

    public static String ID;
    public static String PW;
    public static String DB_NAME;
    JPanel loginPanel;


    public LoginFrame() {
        setTitle("데이터베이스 정보 입력");
        setSize(500, 180);
        setLocationRelativeTo(null);
        id = new JTextField(20);
        pw = new JTextField(20);
        dbName = new JTextField(20);


        JPanel idPanel = new JPanel();
        idPanel.add(new JLabel("Username : "));
        idPanel.add(id);
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));

        JPanel pwPanel = new JPanel();
        pwPanel.add(new JLabel("Password : "));
        pwPanel.add(pw);
        pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.X_AXIS));

        JPanel dbnamePanel = new JPanel();
        dbnamePanel.add(new JLabel("Database name : "));
        dbnamePanel.add(dbName);
        dbnamePanel.setLayout(new BoxLayout(dbnamePanel, BoxLayout.X_AXIS));
        JPanel btnPanel = new JPanel();
        loginBtn = new JButton("접속");
        loginBtn.addActionListener(this);
        btnPanel.add(loginBtn);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        loginPanel = new JPanel();
        JLabel infoMsg = new JLabel("접속하려는 DB의 username, password와 DB 이름을 입력해주세요.");
        loginPanel.add(infoMsg);
        JLabel alertMsg = new JLabel("정확하게 입력하지 않으면 진행되지 않습니다.");
        loginPanel.add(alertMsg);

        loginPanel.add(idPanel);
        loginPanel.add(pwPanel);
        loginPanel.add(dbnamePanel);
        loginPanel.add(btnPanel);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));



        add("North", loginPanel);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //"정보 추가하기" 버튼 클릭시 수행
        ID = id.getText();
        PW = pw.getText();
        DB_NAME = dbName.getText();

        try {
            JDBC jdbc = new JDBC(ID, PW, DB_NAME);
            Connection conn = jdbc.connectJDBC();
            conn.beginRequest();
            MainFrame mainFrame = new MainFrame();
            dispose();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "ID 또는 PW 또는 DB name이 잘못되었습니다.", "경고", JOptionPane.WARNING_MESSAGE);
            exception.printStackTrace();
        }
    }

}


