package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Tạo bảng users
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(20) NOT NULL UNIQUE,
                    password_hash VARCHAR(64) NOT NULL,
                    wins INT DEFAULT 0,
                    losses INT DEFAULT 0,
                    draws INT DEFAULT 0,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Tạo bảng matches (lịch sử trận đấu)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS matches (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    player1 VARCHAR(20) NOT NULL,
                    player2 VARCHAR(20) NOT NULL,
                    winner VARCHAR(20),
                    total_moves INT DEFAULT 0,
                    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            System.out.println("[DB] Khởi tạo database thành công.");
        } catch (SQLException e) {
            System.err.println("[DB LỖI] Không thể khởi tạo database: " + e.getMessage());
        }
    }

    public static String getCreateDatabaseSQL() {
        return "CREATE DATABASE IF NOT EXISTS caro_game CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
    }
}
