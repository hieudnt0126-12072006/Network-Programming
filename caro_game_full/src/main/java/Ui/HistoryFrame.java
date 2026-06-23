package Ui;

import Dao.MatchDAO;
import Model.Match;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryFrame extends JFrame {
    public HistoryFrame(String username) {
        setTitle("Lịch Sử Trận Đấu - " + username);
        setSize(620, 420);
        setLocationRelativeTo(null);

        String[] cols = {"#", "Đối Thủ", "Kết Quả", "Số Nước", "Thời Gian"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        MatchDAO dao = new MatchDAO();
        List<Match> history = dao.getMatchesByPlayer(username);
        int idx = 1;
        for (Match m : history) {
            String opp = m.getPlayer1().equals(username) ? m.getPlayer2() : m.getPlayer1();
            String result;
            if (m.getWinner() == null) result = "Hòa";
            else result = m.getWinner().equals(username) ? "Thắng" : "Thua";
            model.addRow(new Object[]{idx++, opp, result, m.getTotalMoves(), m.getPlayedAt()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JLabel lbl = new JLabel("  Tổng " + history.size() + " trận gần nhất", JLabel.LEFT);
        lbl.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(lbl, BorderLayout.SOUTH);
    }
}
