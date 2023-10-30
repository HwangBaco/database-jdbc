package src.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnect {
    private final String url;
    private final String user;
    private final String password;

    public JDBCConnect(String user, String password, String dbname){
        this.url = "jdbc:mysql://localhost:3306/" + dbname + "?servertimezone=UTC url";
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
