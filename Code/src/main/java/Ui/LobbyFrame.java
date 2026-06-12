package Ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LobbyFrame extends JFrame {
    private JLabel lblStatus;
    private Timer dotTimer;

    public LobbyFrame(ActionListener onCancel) {
        setTitle("Sảnh Chờ Ghép Trận");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(20, 20, 30));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(20, 20, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        JLabel lblTitle = new JLabel("♟ CARO ONLINE", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.CYAN);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);

        panel.add(Box.createVerticalStrut(20));

        lblStatus = new JLabel("Đang tìm đối thủ", JLabel.CENTER);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        lblStatus.setForeground(Color.ORANGE);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblStatus);

        panel.add(Box.createVerticalStrut(25));

        JButton btnCancel = new JButton("✕  Hủy tìm trận");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancel.setBackground(new Color(180, 40, 40));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancel.setMaximumSize(new Dimension(180, 38));
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnCancel.addActionListener(onCancel);
        panel.add(btnCancel);

        add(panel, BorderLayout.CENTER);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(Color.CYAN);
        progressBar.setBackground(new Color(40, 40, 50));
        progressBar.setPreferredSize(new Dimension(0, 5));
        add(progressBar, BorderLayout.SOUTH);

        final String[] dots = {"", ".", "..", "..."};
        final int[] idx = {0};
        dotTimer = new Timer(500, e -> {
            idx[0] = (idx[0] + 1) % dots.length;
            lblStatus.setText("Đang tìm đối thủ" + dots[idx[0]]);
        });
        dotTimer.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                dotTimer.stop();
            }
        });
    }

    @Override
    public void dispose() {
        if (dotTimer != null) dotTimer.stop();
        super.dispose();
    }
}
