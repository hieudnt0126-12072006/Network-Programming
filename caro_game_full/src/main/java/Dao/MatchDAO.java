package Dao;

import Database.DatabaseConnection;
import Model.Match;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    public void saveMatch(Match match) {
        String sql = "INSERT INTO matches (player1, player2, winner, total_moves) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, match.getPlayer1());
            ps.setString(2, match.getPlayer2());
            ps.setString(3, match.getWinner());
            ps.setInt(4, match.getTotalMoves());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Match> getMatchesByPlayer(String username) {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT * FROM matches WHERE player1 = ? OR player2 = ? ORDER BY played_at DESC LIMIT 20";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Match m = new Match();
                m.setId(rs.getInt("id"));
                m.setPlayer1(rs.getString("player1"));
                m.setPlayer2(rs.getString("player2"));
                m.setWinner(rs.getString("winner"));
                m.setTotalMoves(rs.getInt("total_moves"));
                m.setPlayedAt(rs.getString("played_at"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
