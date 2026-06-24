package Ui;

import Client.CaroClient;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JLabel lblError;
    private final CaroClient client;

    public LoginFrame(CaroClient client) {
        this.client = client;
        setTitle("Đăng Nhập - Cờ Caro Online");
        setSize(380, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(22, 22, 35));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(main);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("♟ CỜ CARO ONLINE", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(0, 200, 220));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        main.add(title, g);

        g.gridy = 1; g.gridwidth = 1;
        JLabel l1 = new JLabel("Tên đăng nhập:");
        l1.setForeground(Color.WHITE);
        g.gridx = 0; main.add(l1, g);
        txtUsername = new JTextField(15);
        g.gridx = 1; main.add(txtUsername, g);

        g.gridy = 2;
        JLabel l2 = new JLabel("Mật khẩu:");
        l2.setForeground(Color.WHITE);
        g.gridx = 0; main.add(l2, g);
        txtPassword = new JPasswordField(15);
        g.gridx = 1; main.add(txtPassword, g);

        JButton btnLogin = makeButton("Đăng Nhập", new Color(0, 120, 180));
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        main.add(btnLogin, g);

        JButton btnRegister = makeButton("Chưa có tài khoản? Đăng Ký", new Color(50, 50, 70));
        g.gridy = 4;
        main.add(btnRegister, g);

        lblError = new JLabel("", JLabel.CENTER);
        lblError.setForeground(Color.RED);
        g.gridy = 5;
        main.add(lblError, g);

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> { client.showRegister(); });
        txtPassword.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String u = txtUsername.getText().trim();
        String p = new String(txtPassword.getPassword());
        if (u.isEmpty() || p.isEmpty()) { showError("Vui lòng điền đầy đủ thông tin!"); return; }
        showError("Đang kết nối...");
        client.sendLogin(u, p);
    }

    public void showError(String msg) { lblError.setText(msg); }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
