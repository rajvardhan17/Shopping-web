package example;

import java.sql.Connection;
import java.sql.SQLException;
import db.DBConnection;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnection.getConnection();
    }
}
