package Dao;

import Database.DatabaseConnection;
import Model.User;
import java.sql.*;

public class UserDAO {

    public boolean register(String username, String passwordHash) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // username đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setWins(rs.getInt("wins"));
                u.setLosses(rs.getInt("losses"));
                u.setDraws(rs.getInt("draws"));
                u.setCreatedAt(rs.getString("created_at"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void incrementWins(String username) {
        updateStat(username, "wins");
    }

    public void incrementLosses(String username) {
        updateStat(username, "losses");
    }

    public void incrementDraws(String username) {
        updateStat(username, "draws");
    }

    private void updateStat(String username, String column) {
        String sql = "UPDATE users SET " + column + " = " + column + " + 1 WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
