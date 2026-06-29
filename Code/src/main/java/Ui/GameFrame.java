<<<<<<< HEAD
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
=======
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
    private static final int CELL_SIZE = 32;

    private final JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
    private final Board board;

    private final JTextArea txtChatArea;
    private final JTextField txtChatInput;
    private final JLabel lblStatus;
    private final JLabel lblTimer;

    private final CaroClient client;
    private final String mySymbol;
    private boolean isMyTurn;
    private final String opponentName;
    private boolean gameOver = false;

    // Simple turn timer (UI only)
    private Timer uiTimer;
    private int timerSeconds = 60;

    public GameFrame(CaroClient client, String symbol, boolean turn, String opponentName) {
        this.client = client;
        this.mySymbol = symbol;
        this.isMyTurn = turn;
        this.opponentName = opponentName;
        this.board = new Board(BOARD_SIZE);

        setTitle("Cờ Caro Online - " + client.getUsername() + " (" + symbol + ") vs " + opponentName);
        setSize(BOARD_SIZE * CELL_SIZE + 300, BOARD_SIZE * CELL_SIZE + 60);
        setMinimumSize(new Dimension(900, 720));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(20, 20, 30));

        // Status bar top
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(30, 30, 45));
        topBar.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        lblStatus = new JLabel("", JLabel.CENTER);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 14));
        topBar.add(lblStatus, BorderLayout.CENTER);

        lblTimer = new JLabel("60s", JLabel.RIGHT);
        lblTimer.setFont(new Font("Arial", Font.BOLD, 14));
        lblTimer.setForeground(Color.ORANGE);
        topBar.add(lblTimer, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Board
        JPanel pnlBoard = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE, 1, 1));
        pnlBoard.setBackground(new Color(180, 140, 80)); // gỗ màu vàng

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                final int r = i, c = j;
                JButton btn = new JButton("");
                btn.setFont(new Font("Arial", Font.BOLD, 13));
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setBorder(BorderFactory.createLineBorder(new Color(140, 100, 50)));
                btn.setBackground(new Color(240, 210, 160));
                btn.setFocusPainted(false);
                btn.addActionListener(e -> handleCellClick(r, c));
                buttons[i][j] = btn;
                pnlBoard.add(btn);
            }
        }

        JScrollPane boardScroll = new JScrollPane(pnlBoard);
        boardScroll.setBorder(null);
        add(boardScroll, BorderLayout.CENTER);

        // Right panel - chat
        JPanel pnlRight = new JPanel(new BorderLayout(4, 4));
        pnlRight.setPreferredSize(new Dimension(260, 0));
        pnlRight.setBackground(new Color(25, 25, 38));
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(60, 60, 80)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel chatTitle = new JLabel("💬 Chat trong trận", JLabel.CENTER);
        chatTitle.setForeground(Color.CYAN);
        chatTitle.setFont(new Font("Arial", Font.BOLD, 13));
        pnlRight.add(chatTitle, BorderLayout.NORTH);

        txtChatArea = new JTextArea();
        txtChatArea.setEditable(false);
        txtChatArea.setLineWrap(true);
        txtChatArea.setWrapStyleWord(true);
        txtChatArea.setBackground(new Color(30, 30, 45));
        txtChatArea.setForeground(Color.WHITE);
        txtChatArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane chatScroll = new JScrollPane(txtChatArea);
        chatScroll.setBorder(null);
        pnlRight.add(chatScroll, BorderLayout.CENTER);

        JPanel chatInput = new JPanel(new BorderLayout(4, 0));
        chatInput.setBackground(new Color(25, 25, 38));
        txtChatInput = new JTextField();
        txtChatInput.setBackground(new Color(40, 40, 55));
        txtChatInput.setForeground(Color.WHITE);
        txtChatInput.setCaretColor(Color.WHITE);
        txtChatInput.addActionListener(e -> sendChat());
        JButton btnSend = new JButton("Gửi");
        btnSend.setBackground(new Color(0, 110, 160));
        btnSend.setForeground(Color.WHITE);
        btnSend.setBorderPainted(false);
        btnSend.addActionListener(e -> sendChat());
        chatInput.add(txtChatInput, BorderLayout.CENTER);
        chatInput.add(btnSend, BorderLayout.EAST);
        pnlRight.add(chatInput, BorderLayout.SOUTH);

        add(pnlRight, BorderLayout.EAST);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                int r = JOptionPane.showConfirmDialog(GameFrame.this,
                    "Bạn có chắc muốn thoát? (Bạn sẽ thua nếu trận đang diễn ra)",
                    "Xác nhận thoát", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    client.leftRoom();
                }
            }
        });

        updateStatusLabel();
        startTimer();
    }

    private void handleCellClick(int r, int c) {
        if (gameOver || !isMyTurn) {
            if (!isMyTurn && !gameOver) JOptionPane.showMessageDialog(this, "Đang là lượt của đối thủ!");
            return;
        }
        if (!board.setMove(r, c, mySymbol)) return;

        renderCell(r, c, mySymbol);

        Message msg = new Message(MessageType.MOVE);
        msg.setSender(client.getUsername());
        msg.setSymbol(mySymbol);
        msg.setX(r); msg.setY(c);
        client.sendMessage(msg);

        if (GameLogic.checkWin(board, r, c, mySymbol)) {
            // Server sẽ gửi GAME_OVER - không cần xử lý ở đây
        } else {
            isMyTurn = false;
            resetTimer();
            updateStatusLabel();
        }
    }

    public void updateOpponentMove(int x, int y, String oppSymbol) {
        board.setMove(x, y, oppSymbol);
        renderCell(x, y, oppSymbol);
        if (!GameLogic.checkWin(board, x, y, oppSymbol)) {
            isMyTurn = true;
            resetTimer();
            updateStatusLabel();
        }
    }

    public void handleGameOver(String message, String winner) {
        gameOver = true;
        isMyTurn = false;
        stopTimer();
        lockBoard();

        boolean iWon = winner != null && winner.equals(client.getUsername());
        boolean isDraw = "DRAW".equals(winner);

        lblStatus.setText(isDraw ? "HÒA! " : (iWon ? "🏆 BẠN THẮNG!" : "💀 BẠN THUA!"));
        lblStatus.setForeground(isDraw ? Color.YELLOW : (iWon ? new Color(0, 220, 100) : Color.RED));

        Object[] opts = {"🔄 Về Sảnh", "❌ Thoát"};
        int choice = JOptionPane.showOptionDialog(this, message + "\n\nBạn muốn làm gì?",
                "Trận đấu kết thúc", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opts, opts[0]);
        if (choice == JOptionPane.YES_OPTION) client.leftRoom();
        else System.exit(0);
    }

    public void handleOpponentLeft(String notification) {
        gameOver = true;
        stopTimer();
        lockBoard();
        lblStatus.setText("Đối thủ đã rời phòng.");
        lblStatus.setForeground(Color.GRAY);

        Object[] opts = {"🔄 Về Sảnh", "❌ Thoát"};
        int choice = JOptionPane.showOptionDialog(this,
                (notification != null ? notification : "Đối thủ đã thoát.") + "\n\nBạn muốn làm gì?",
                "Đối thủ đã thoát", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                null, opts, opts[0]);
        if (choice == JOptionPane.YES_OPTION) client.leftRoom();
        else System.exit(0);
    }

    private void sendChat() {
        String text = txtChatInput.getText().trim();
        if (text.isEmpty()) return;
        txtChatArea.append("Tôi: " + text + "\n");
        txtChatInput.setText("");
        Message msg = new Message(MessageType.CHAT);
        msg.setSender(client.getUsername());
        msg.setContent(text);
        client.sendMessage(msg);
        scrollChatToBottom();
    }

    public void appendChat(String line) {
        txtChatArea.append(line + "\n");
        scrollChatToBottom();
    }

    private void scrollChatToBottom() {
        txtChatArea.setCaretPosition(txtChatArea.getDocument().getLength());
    }

    private void renderCell(int r, int c, String sym) {
        buttons[r][c].setText(sym);
        buttons[r][c].setForeground("X".equals(sym) ? new Color(200, 30, 30) : new Color(30, 80, 200));
        buttons[r][c].setBackground(new Color(220, 190, 140));
        buttons[r][c].setEnabled(false);
    }

    private void lockBoard() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                buttons[i][j].setEnabled(false);
        txtChatInput.setEnabled(false);
    }

    private void updateStatusLabel() {
        String opp = "vs " + opponentName;
        if (isMyTurn) {
            lblStatus.setText(opp + "  |  ✅ LƯỢT CỦA BẠN (" + mySymbol + ")");
            lblStatus.setForeground(new Color(0, 200, 100));
        } else {
            lblStatus.setText(opp + "  |  ⏳ Đang chờ đối thủ...");
            lblStatus.setForeground(new Color(220, 150, 0));
        }
    }

    private void startTimer() {
        timerSeconds = 60;
        uiTimer = new Timer(1000, e -> {
            timerSeconds--;
            lblTimer.setText(timerSeconds + "s");
            if (timerSeconds <= 10) lblTimer.setForeground(Color.RED);
            else lblTimer.setForeground(Color.ORANGE);
            if (timerSeconds <= 0) stopTimer();
        });
        uiTimer.start();
    }

    private void resetTimer() {
        stopTimer();
        startTimer();
    }

    private void stopTimer() {
        if (uiTimer != null) { uiTimer.stop(); uiTimer = null; }
    }
}
>>>>>>> main
