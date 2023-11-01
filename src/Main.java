package src;

import src.JDBC.JDBC;
import javax.swing.JFrame;
import java.sql.*;

public class Main extends JFrame {

    /*
    * 사용자에 따라 id / pw / dbname 입력
    * */
    public static final String ID = "root";
    public static final String PW = "password";
    public static final String DB_NAME = "company";
    public static void main(String[] args) {
        new MainFrame();
    }
}
