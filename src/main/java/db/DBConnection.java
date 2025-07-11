package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "shopping";
    private static final String FULL_DB_URL = DB_URL + DB_NAME;
    private static final String USER = "root";
    private static final String PASS = "admin@2204";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(FULL_DB_URL, USER, PASS);
    }

    public static Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
