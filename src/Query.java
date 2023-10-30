package src;

import java.sql.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Query {
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;
    String sql = null;
    static String url = "jdbc:mysql://localhost:3306/company_forhw?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    static String dbacct = "root";
    static String passwrd = "newabt12!";

    public static void accDb() throws SQLException {
        conn = DriverManager.getConnection(url, dbacct, passwrd);
    }
}
