package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/restomanagement";
    private static final String USERNAME = "Stephen001";
    private static final String PASSWORD = "7gs91.i_Co6(4Yq/";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Gagal terhubung ke database", e);
        }
    }
}
