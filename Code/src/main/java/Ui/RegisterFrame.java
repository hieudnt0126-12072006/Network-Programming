// Thành viên thực hiện: Nguyễn Trần Đình Hiệu - Nhiệm vụ: Giao diện UI
package Ui;

import Client.CaroClient;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JPasswordField txtConfirm;
    private final JLabel lblMsg;
    private final CaroClient client;

    public RegisterFrame(CaroClient client) {
        this.client = client;
        setTitle("Đăng Ký Tài Khoản");
        setSize(380, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(22, 22, 35));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(main);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("TẠO TÀI KHOẢN MỚI", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(0, 200, 120));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        main.add(title, g);

        g.gridwidth = 1;
        String[] labels = {"Tên đăng nhập:", "Mật khẩu (≥6 ký tự):", "Xác nhận mật khẩu:"};
        JComponent[] fields = {
            txtUsername = new JTextField(15),
            txtPassword = new JPasswordField(15),
            txtConfirm  = new JPasswordField(15)
        };
        for (int i = 0; i < 3; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.WHITE);
            g.gridx = 0; g.gridy = i + 1; main.add(lbl, g);
            g.gridx = 1; main.add(fields[i], g);
        }

        JButton btnReg = new JButton("Đăng Ký");
        btnReg.setBackground(new Color(0, 150, 80));
        btnReg.setForeground(Color.WHITE);
        btnReg.setFocusPainted(false);
        btnReg.setBorderPainted(false);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        main.add(btnReg, g);

        lblMsg = new JLabel("", JLabel.CENTER);
        lblMsg.setForeground(Color.RED);
        g.gridy = 5;
        main.add(lblMsg, g);

        btnReg.addActionListener(e -> {
            String u = txtUsername.getText().trim();
            String p = new String(txtPassword.getPassword());
            String c = new String(txtConfirm.getPassword());
            if (u.isEmpty() || p.isEmpty()) { showError("Điền đủ thông tin!"); return; }
            if (!p.equals(c)) { showError("Mật khẩu không khớp!"); return; }
            showError("Đang xử lý...");
            client.sendRegister(u, p);
        });
    }

    public void showError(String msg) { lblMsg.setForeground(Color.RED); lblMsg.setText(msg); }
    public void showSuccess(String msg) { lblMsg.setForeground(new Color(0, 200, 100)); lblMsg.setText(msg); }
}
