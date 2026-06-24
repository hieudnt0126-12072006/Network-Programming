package Ui;

import Database.StatisticDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StatisticFrame extends JFrame {
    public StatisticFrame() {
        setTitle("Bảng Xếp Hạng");
        setSize(500, 400);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("🏆 BẢNG XẾP HẠNG", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 6, 0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Hạng", "Tên Người Chơi", "Thắng", "Thua", "Hòa"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        StatisticDAO dao = new StatisticDAO();
        List<Object[]> rows = dao.getLeaderboard(20);
        int rank = 1;
        for (Object[] row : rows) {
            model.addRow(new Object[]{rank++, row[0], row[1], row[2], row[3]});
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
