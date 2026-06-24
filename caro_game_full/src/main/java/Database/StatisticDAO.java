package Database;

import Model.Match;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAO {

    public List<Match> getRecentMatches(int limit) {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT * FROM matches ORDER BY played_at DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Match m = new Match();
                m.setId(rs.getInt("id"));
                m.setPlayer1(rs.getString("player1"));
                m.setPlayer2(rs.getString("player2"));
                m.setWinner(rs.getString("winner"));
                m.setTotalMoves(rs.getInt("total_moves"));
                m.setPlayedAt(rs.getString("played_at"));
                matches.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public List<Object[]> getLeaderboard(int limit) {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT username, wins, losses, draws FROM users ORDER BY wins DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rows.add(new Object[]{
                    rs.getString("username"),
                    rs.getInt("wins"),
                    rs.getInt("losses"),
                    rs.getInt("draws")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
}
