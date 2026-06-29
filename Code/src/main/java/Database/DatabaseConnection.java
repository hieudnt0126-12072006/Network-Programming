package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Driver SQL Server
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USERNAME,
                        DatabaseConfig.PASSWORD
                );

                System.out.println("[DB] Kết nối SQL Server thành công.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQL Server JDBC Driver không tìm thấy. Kiểm tra pom.xml.", e);
            }
        }
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Đã đóng kết nối SQL Server.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}