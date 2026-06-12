package Ui;

import Client.CaroClient;
import Game.Board;
import Game.GameLogic;
import Model.Message;
import Model.MessageType;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private static final int BOARD_SIZE = 20;
    private JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private Board board;

    private JTextArea txtChatArea;
    private JTextField txtChatInput;
    private JLabel lblStatus;

    private CaroClient client;
    private String mySymbol;
    private boolean isMyTurn;
    private String opponentName;
    private boolean gameOver = false;

    public GameFrame(CaroClient client, String symbol, boolean turn, String opponentName) {
        this.client = client;
        this.mySymbol = symbol;
        this.isMyTurn = turn;
        this.opponentName = opponentName;
        this.board = new Board(BOARD_SIZE);

        setTitle("Trận Đấu Caro Online - " + client.getUsername());
        setSize(950, 720);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        lblStatus = new JLabel("", JLabel.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 15));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        updateStatusLabel();
        add(lblStatus, BorderLayout.NORTH);

        JPanel pnlBoard = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                final int r = i;
                final int c = j;
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 11));
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(e -> handleCellClick(r, c));
                pnlBoard.add(buttons[i][j]);
            }
        }
        add(pnlBoard, BorderLayout.CENTER);

        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setPreferredSize(new Dimension(260, 0));
        pnlRight.setBorder(BorderFactory.createTitledBorder("Phòng chat đối thủ"));

        txtChatArea = new JTextArea();
        txtChatArea.setEditable(false);
        txtChatArea.setLineWrap(true);
        pnlRight.add(new JScrollPane(txtChatArea), BorderLayout.CENTER);

        txtChatInput = new JTextField();
        txtChatInput.addActionListener(e -> sendChatMessage());
        pnlRight.add(txtChatInput, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);
    }

    private void handleCellClick(int r, int c) {
        if (gameOver) return;
        if (!isMyTurn) {
            JOptionPane.showMessageDialog(this, "Đang là lượt đi của đối thủ!");
            return;
        }
        if (!board.setMove(r, c, mySymbol)) return;

        buttons[r][c].setText(mySymbol);
        buttons[r][c].setForeground(mySymbol.equals("X") ? Color.RED : Color.BLUE);

        Message msg = new Message(MessageType.MOVE);
        msg.setSender(client.getUsername());
        msg.setSymbol(mySymbol);
        msg.setX(r);
        msg.setY(c);
        client.sendMessage(msg);

        if (GameLogic.checkWin(board, r, c, mySymbol)) {
            endGame("🏆 CHIẾN THẮNG!", "Chúc mừng! Bạn đã thắng cuộc.");
        } else {
            isMyTurn = false;
            updateStatusLabel();
        }
    }

    public void updateOpponentMove(int x, int y, String oppSymbol) {
        board.setMove(x, y, oppSymbol);
        buttons[x][y].setText(oppSymbol);
        buttons[x][y].setForeground(oppSymbol.equals("X") ? Color.RED : Color.BLUE);

        if (GameLogic.checkWin(board, x, y, oppSymbol)) {
            endGame("💀 THẤT BẠI", "Rất tiếc! Bạn đã thua trận này.");
        } else {
            isMyTurn = true;
            updateStatusLabel();
        }
    }

    private void sendChatMessage() {
        String text = txtChatInput.getText().trim();
        if (text.isEmpty()) return;

        txtChatArea.append("Tôi: " + text + "\n");
        txtChatInput.setText("");

        Message msg = new Message(MessageType.CHAT);
        msg.setSender(client.getUsername());
        msg.setContent(text);
        client.sendMessage(msg);
    }

    public void appendChat(String rawMessage) {
        txtChatArea.append(rawMessage + "\n");
    }

    public void handleOpponentLeft(String notification) {
        gameOver = true;
        isMyTurn = false;
        lockBoard();
        lblStatus.setText("ĐỐI THỦ ĐÃ THOÁT KHỎI PHÒNG.");
        lblStatus.setForeground(Color.GRAY);

        Object[] options = {"🔄 Ghép trận mới", "❌ Thoát game"};
        int choice = JOptionPane.showOptionDialog(this,
                notification + "\n\nBạn muốn làm gì tiếp theo?",
                "Đối thủ đã thoát",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) client.requestRematch();
        else client.cancelMatchmaking();
    }

    private void endGame(String statusText, String message) {
        gameOver = true;
        isMyTurn = false;
        lockBoard();
        lblStatus.setText("TRẬN ĐẤU KẾT THÚC: " + statusText);
        lblStatus.setForeground(new Color(128, 0, 128));

        Object[] options = {"🔄 Ghép trận mới", "❌ Thoát game"};
        int choice = JOptionPane.showOptionDialog(this,
                message + "\n\nBạn muốn làm gì tiếp theo?",
                "Kết thúc trận đấu",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) client.requestRematch();
        else client.cancelMatchmaking();
    }

    private void lockBoard() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                buttons[i][j].setEnabled(false);
        txtChatInput.setEnabled(false);
    }

    private void updateStatusLabel() {
        if (isMyTurn) {
            lblStatus.setText("Đang đối đầu với: " + opponentName + " | LƯỢT ĐÁNH CỦA BẠN (" + mySymbol + ")");
            lblStatus.setForeground(new Color(0, 128, 0));
        } else {
            lblStatus.setText("Đang đối đầu với: " + opponentName + " | ĐANG CHỜ ĐỐI THỦ ĐÁNH...");
            lblStatus.setForeground(Color.RED);
        }
    }
}
