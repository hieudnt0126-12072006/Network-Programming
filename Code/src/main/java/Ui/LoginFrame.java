package Ui;

import Client.CaroClient;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtName;
    private JLabel lblError;
    private CaroClient client;

    public LoginFrame(CaroClient client) {
        this.client = client;
        setTitle("Đăng Nhập - UDM_17");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("GAME CỜ CARO ONLINE", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Color.CYAN);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        JLabel lblName = new JLabel("Tên người chơi:");
        lblName.setForeground(Color.WHITE);
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(lblName, gbc);

        txtName = new JTextField(15);
        gbc.gridx = 1;
        add(txtName, gbc);

        JButton btnLogin = new JButton("Vào Game");
        btnLogin.setBackground(Color.DARK_GRAY);
        btnLogin.setForeground(Color.GREEN);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnLogin, gbc);

        lblError = new JLabel("", JLabel.CENTER);
        lblError.setForeground(Color.RED);
        gbc.gridy = 3;
        add(lblError, gbc);

        btnLogin.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                lblError.setText("Tên không được để trống!");
            } else {
                lblError.setText("Đang kết nối...");
                client.startConnection(name);
            }
        });
    }

    public void showError(String msg) {
        lblError.setText(msg);
    }
}