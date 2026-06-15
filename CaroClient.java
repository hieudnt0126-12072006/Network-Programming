package Client;

import Model.Message;
import Model.MessageType;
import Ui.GameFrame;
import Ui.LobbyFrame;
import Ui.LoginFrame;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import javax.swing.SwingUtilities;

/**
 * CaroClient - Client Socket cho người chơi
 * Chức năng chính:
 * 1. Kết nối đến Server qua TCP Socket
 * 2. Bắt tín hiệu từ Server (nhận nước đi đối thủ, chat, thông báo)
 * 3. Gửi tọa độ nước đi lên Server
 * 4. Cập nhật màn hình khi nhận nước đi từ đối thủ
 */
public class CaroClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 8080;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson = new Gson();

    private String username;
    private LoginFrame loginFrame;
    private LobbyFrame lobbyFrame;
    private GameFrame gameFrame;
    private volatile boolean isConnected = false;
    private Thread listeningThread;

    public CaroClient() {
        loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
    }

    /**
     * Kết nối đến Server và bắt đầu lắng nghe tin nhắn
     * @param name Tên người dùng đăng nhập
     */
    public void startConnection(String name) {
        this.username = name;
        try {
            // Tạo kết nối Socket đến Server
            socket = new Socket(SERVER_IP, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            isConnected = true;

            // Gửi tin nhắn đăng nhập lên Server
            Message loginMsg = new Message(MessageType.LOGIN);
            loginMsg.setSender(username);
            sendMessage(loginMsg);

            // Đóng màn hình đăng nhập, hiển thị sảnh chờ
            loginFrame.setVisible(false);
            showLobby();

            // Bắt đầu luồng lắng nghe tin nhắn từ Server
            listeningThread = new Thread(this::listenServer);
            listeningThread.start();

        } catch (Exception e) {
            isConnected = false;
            loginFrame.showError("Không kết nối được đến Server. Vui lòng mở CaroServer trước!");
        }
    }

    /**
     * Yêu cầu ghép trận mới sau khi trận đấu kết thúc
     */
    public void requestRematch() {
        SwingUtilities.invokeLater(() -> {
            if (gameFrame != null) { gameFrame.dispose(); gameFrame = null; }
            showLobby();
        });
        if (isConnected) {
            Message loginMsg = new Message(MessageType.LOGIN);
            loginMsg.setSender(username);
            sendMessage(loginMsg);
        }
    }

    /**
     * Hủy tìm trận và ngắt kết nối
     */
    public void cancelMatchmaking() {
        Message quitMsg = new Message(MessageType.QUIT);
        quitMsg.setSender(username);
        sendMessage(quitMsg);

        SwingUtilities.invokeLater(() -> {
            if (lobbyFrame != null) { lobbyFrame.dispose(); lobbyFrame = null; }
            loginFrame.showError("");
            loginFrame.setVisible(true);
        });
        closeConnection();
    }

    /**
     * Hiển thị sảnh chờ (Lobby)
     */
    private void showLobby() {
        lobbyFrame = new LobbyFrame(e -> cancelMatchmaking());
        lobbyFrame.setVisible(true);
    }

    /**
     * Lắng nghe tin nhắn từ Server - Bắt tín hiệu
     * Đây là luồng chạy liên tục để nhận các tin nhắn từ Server
     * Xử lý các loại tin nhắn: START_GAME, MOVE, CHAT, QUIT
     */
    private void listenServer() {
        try {
            String line;
            while (!Thread.currentThread().isInterrupted() && (line = in.readLine()) != null) {
                Message msg = gson.fromJson(line, Message.class);
                if (msg == null) continue;

                switch (msg.getType()) {
                    case START_GAME:
                        // Server thông báo bắt đầu trận đấu
                        final String symbol = msg.getSymbol();
                        final boolean turn = msg.isYourTurn();
                        final String oppName = msg.getContent();

                        SwingUtilities.invokeLater(() -> {
                            if (lobbyFrame != null) { lobbyFrame.dispose(); lobbyFrame = null; }
                            gameFrame = new GameFrame(this, symbol, turn, oppName);
                            gameFrame.setVisible(true);
                        });
                        break;

                    case MOVE:
                        // Nhận nước đi từ đối thủ - Cập nhật lên màn hình
                        if (gameFrame != null) {
                            final int x = msg.getX();
                            final int y = msg.getY();
                            final String sym = msg.getSymbol();
                            SwingUtilities.invokeLater(() -> gameFrame.updateOpponentMove(x, y, sym));
                        }
                        break;

                    case CHAT:
                        // Nhận tin nhắn chat từ đối thủ
                        if (gameFrame != null) {
                            final String chatLine = msg.getSender() + ": " + msg.getContent();
                            SwingUtilities.invokeLater(() -> gameFrame.appendChat(chatLine));
                        }
                        break;

                    case QUIT:
                        // Đối thủ thoát game
                        if (gameFrame != null) {
                            final String content = msg.getContent();
                            SwingUtilities.invokeLater(() -> gameFrame.handleOpponentLeft(content));
                        }
                        break;
                }
            }
        } catch (IOException e) {
            if (!Thread.currentThread().isInterrupted()) {
                isConnected = false;
                if (gameFrame != null) {
                    SwingUtilities.invokeLater(() -> gameFrame.appendChat("[HỆ THỐNG] Mất kết nối đến máy chủ."));
                }
            }
        }
    }

    /**
     * Gửi tin nhắn đến Server - Gửi tọa độ nước đi, chat, etc.
     * @param msg Tin nhắn cần gửi (đã được đóng gói bằng Gson)
     */
    public void sendMessage(Message msg) {
        if (out != null && isConnected) {
            out.println(gson.toJson(msg));
        }
    }

    /**
     * Đóng kết nối Socket và giải phóng tài nguyên
     */
    private void closeConnection() {
        isConnected = false;
        try {
            // Ngắt luồng lắng nghe
            if (listeningThread != null && listeningThread.isAlive()) {
                listeningThread.interrupt();
            }
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("[LỖI] Không thể đóng kết nối: " + e.getMessage());
        }
    }

    /**
     * Thử kết nối lại đến Server khi mất kết nối
     * @return true nếu kết nối lại thành công, false nếu thất bại
     */
    public boolean reconnect() {
        try {
            closeConnection();
            Thread.sleep(1000); // Đợi 1 giây trước khi kết nối lại

            socket = new Socket(SERVER_IP, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            isConnected = true;

            // Gửi lại tin nhắn đăng nhập sau khi kết nối lại
            Message loginMsg = new Message(MessageType.LOGIN);
            loginMsg.setSender(username);
            sendMessage(loginMsg);

            // Khởi động lại luồng lắng nghe
            listeningThread = new Thread(this::listenServer);
            listeningThread.start();

            return true;
        } catch (Exception e) {
            isConnected = false;
            System.err.println("[LỖI] Không thể kết nối lại: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy tên người dùng hiện tại
     * @return Tên người dùng
     */
    public String getUsername() { return username; }

    /**
     * Kiểm tra trạng thái kết nối
     * @return true nếu đã kết nối, false nếu ngắt kết nối
     */
    public boolean isConnected() { return isConnected; }

    /**
     * Main method - Khởi chạy ứng dụng Client
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CaroClient::new);
    }
}
