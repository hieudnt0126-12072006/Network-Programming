package Ui;

import Client.CaroClient;
import Model.Room;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class LobbyFrame extends JFrame {
    private final CaroClient client;
    private final DefaultTableModel tableModel;
    private final JTable roomTable;
    private final JLabel lblStatus;
    private final JButton btnJoin;
    private final JButton btnCreate;
    private final JButton btnRefresh;
    private List<Room> currentRooms;

    public LobbyFrame(CaroClient client) {
        this.client = client;
        setTitle("Sảnh Chờ - Cờ Caro Online  [" + client.getUsername() + "]");
        setSize(680, 480);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBackground(new Color(18, 18, 28));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(main);

        // Header
        JLabel title = new JLabel("♟ SẢNH PHÒNG CARO", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(0, 200, 220));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        main.add(title, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID Phòng", "Tên Phòng", "Chủ Phòng", "Trạng Thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.setRowHeight(28);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 13));
        roomTable.setBackground(new Color(30, 30, 45));
        roomTable.setForeground(Color.WHITE);
        roomTable.setGridColor(new Color(60, 60, 80));
        roomTable.getTableHeader().setBackground(new Color(40, 40, 60));
        roomTable.getTableHeader().setForeground(Color.CYAN);

        JScrollPane scroll = new JScrollPane(roomTable);
        scroll.setBackground(new Color(30, 30, 45));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 100)));
        main.add(scroll, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottom = new JPanel(new BorderLayout(8, 4));
        bottom.setBackground(new Color(18, 18, 28));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        btnPanel.setBackground(new Color(18, 18, 28));

        btnCreate  = makeBtn("➕ Tạo Phòng", new Color(0, 130, 70));
        btnJoin    = makeBtn("▶ Vào Phòng", new Color(0, 100, 170));
        btnRefresh = makeBtn("🔄 Làm Mới", new Color(80, 80, 100));

        btnPanel.add(btnCreate);
        btnPanel.add(btnJoin);
        btnPanel.add(btnRefresh);
        bottom.add(btnPanel, BorderLayout.NORTH);

        lblStatus = new JLabel(" ", JLabel.CENTER);
        lblStatus.setForeground(Color.ORANGE);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 13));
        bottom.add(lblStatus, BorderLayout.SOUTH);

        main.add(bottom, BorderLayout.SOUTH);

        // Actions
        btnCreate.addActionListener(e -> doCreateRoom());
        btnJoin.addActionListener(e -> doJoinRoom());
        btnRefresh.addActionListener(e -> client.requestLobbyUpdate());

        roomTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) doJoinRoom();
            }
        });
    }

    private void doCreateRoom() {
        String name = JOptionPane.showInputDialog(this, "Tên phòng:", "Tạo Phòng Mới", JOptionPane.PLAIN_MESSAGE);
        if (name == null) return;
        if (name.trim().isEmpty()) name = client.getUsername() + "'s Room";
        client.sendCreateRoom(name.trim());
        setButtonsEnabled(false);
    }

    private void doJoinRoom() {
        int row = roomTable.getSelectedRow();
        if (row < 0) { showMessage("Vui lòng chọn một phòng!"); return; }
        if (currentRooms == null || row >= currentRooms.size()) return;
        Room room = currentRooms.get(row);
        client.sendJoinRoom(room.getRoomId());
        setButtonsEnabled(false);
    }

    public void updateRoomList(List<Room> rooms) {
        this.currentRooms = rooms;
        tableModel.setRowCount(0);
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                r.getRoomId(), r.getRoomName(), r.getHostUsername(), "Đang chờ"
            });
        }
        if (rooms.isEmpty()) {
            lblStatus.setText("Không có phòng nào. Hãy tạo phòng mới!");
        } else {
            lblStatus.setText("Có " + rooms.size() + " phòng đang chờ. Double-click để vào phòng.");
        }
        setButtonsEnabled(true);
    }

    public void showWaitingForOpponent(String msg) {
        lblStatus.setText(msg != null ? msg : "Đang chờ đối thủ...");
        setButtonsEnabled(false);
    }

    public void showMessage(String msg) {
        lblStatus.setText(msg);
        setButtonsEnabled(true);
    }

    private void setButtonsEnabled(boolean enabled) {
        btnCreate.setEnabled(enabled);
        btnJoin.setEnabled(enabled);
        btnRefresh.setEnabled(enabled);
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
    }
}
