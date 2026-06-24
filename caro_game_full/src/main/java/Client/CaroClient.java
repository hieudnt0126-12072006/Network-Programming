package Client;

import Model.Message;
import Model.MessageType;
import Model.Room;
import Ui.*;
import com.google.gson.Gson;
import javax.swing.SwingUtilities;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class CaroClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 8080;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final Gson gson = new Gson();

    private String username;

    // Frames
    private LoginFrame loginFrame;
    private RegisterFrame registerFrame;
    private LobbyFrame lobbyFrame;
    private GameFrame gameFrame;

    public CaroClient() {
        loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
    }

    // ====== Kết nối đến Server ======
    public boolean connect() {
        if (socket != null && !socket.isClosed()) return true;
        try {
            socket = new Socket(SERVER_IP, PORT);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            new Thread(this::listenServer).start();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ====== Auth ======
    public void sendRegister(String uname, String password) {
        if (!connect()) { showLoginError("Không kết nối được Server!"); return; }
        Message msg = new Message(MessageType.REGISTER);
        msg.setUsername(uname);
        msg.setPassword(password);
        sendMessage(msg);
    }

    public void sendLogin(String uname, String password) {
        if (!connect()) { showLoginError("Không kết nối được Server!"); return; }
        Message msg = new Message(MessageType.LOGIN);
        msg.setUsername(uname);
        msg.setPassword(password);
        sendMessage(msg);
    }

    // ====== Lobby ======
    public void sendCreateRoom(String roomName) {
        Message msg = new Message(MessageType.CREATE_ROOM);
        msg.setRoomName(roomName);
        msg.setSender(username);
        sendMessage(msg);
    }

    public void sendJoinRoom(String roomId) {
        Message msg = new Message(MessageType.JOIN_ROOM);
        msg.setRoomId(roomId);
        msg.setSender(username);
        sendMessage(msg);
    }

    public void requestLobbyUpdate() {
        Message msg = new Message(MessageType.GET_LOBBY);
        msg.setSender(username);
        sendMessage(msg);
    }

    public void leftRoom() {
        Message msg = new Message(MessageType.LEFT_ROOM);
        msg.setSender(username);
        sendMessage(msg);
        SwingUtilities.invokeLater(() -> {
            if (gameFrame != null) { gameFrame.dispose(); gameFrame = null; }
            showLobby();
        });
    }

    // ====== Nhận tin nhắn từ Server ======
    private void listenServer() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                final String raw = line;
                Message msg = gson.fromJson(line, Message.class);
                if (msg == null) continue;

                switch (msg.getType()) {
                    case AUTH_SUCCESS -> SwingUtilities.invokeLater(() -> handleAuthSuccess(msg));
                    case AUTH_FAIL    -> SwingUtilities.invokeLater(() -> handleAuthFail(msg));
                    case LOBBY_UPDATE -> SwingUtilities.invokeLater(() -> handleLobbyUpdate(msg));
                    case ROOM_CREATED -> SwingUtilities.invokeLater(() -> handleRoomCreated(msg));
                    case ROOM_FULL    -> SwingUtilities.invokeLater(() -> showLobbyMessage("Phòng đã đầy hoặc không tồn tại!"));
                    case START_GAME   -> SwingUtilities.invokeLater(() -> handleStartGame(msg));
                    case MOVE         -> SwingUtilities.invokeLater(() -> { if (gameFrame != null) gameFrame.updateOpponentMove(msg.getX(), msg.getY(), msg.getSymbol()); });
                    case GAME_OVER    -> SwingUtilities.invokeLater(() -> { if (gameFrame != null) gameFrame.handleGameOver(msg.getContent(), msg.getSender()); });
                    case CHAT         -> SwingUtilities.invokeLater(() -> { if (gameFrame != null) gameFrame.appendChat(msg.getSender() + ": " + msg.getContent()); });
                    case QUIT         -> SwingUtilities.invokeLater(() -> { if (gameFrame != null) gameFrame.handleOpponentLeft(msg.getContent()); });
                    case ERROR        -> SwingUtilities.invokeLater(() -> showLobbyMessage(msg.getContent()));
                    default -> {}
                }
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> showLoginError("Mất kết nối đến Server!"));
        }
    }

    private void handleAuthSuccess(Message msg) {
        // Nếu là register thành công
        if (msg.getContent() != null && msg.getContent().contains("Đăng ký thành công")) {
            if (registerFrame != null) {
                registerFrame.showSuccess(msg.getContent());
            }
            return;
        }
        // Login thành công
        this.username = msg.getSender();
        if (loginFrame != null) { loginFrame.dispose(); loginFrame = null; }
        if (registerFrame != null) { registerFrame.dispose(); registerFrame = null; }
        showLobby();
    }

    private void handleAuthFail(Message msg) {
        String err = msg.getErrorMessage() != null ? msg.getErrorMessage() : "Lỗi không xác định";
        if (registerFrame != null && registerFrame.isVisible()) {
            registerFrame.showError(err);
        } else {
            showLoginError(err);
        }
    }

    private void handleLobbyUpdate(Message msg) {
        if (lobbyFrame != null && msg.getRooms() != null) {
            lobbyFrame.updateRoomList(msg.getRooms());
        }
    }

    private void handleRoomCreated(Message msg) {
        if (lobbyFrame != null) {
            lobbyFrame.showWaitingForOpponent(msg.getContent());
        }
    }

    private void handleStartGame(Message msg) {
        if (lobbyFrame != null) { lobbyFrame.dispose(); lobbyFrame = null; }
        gameFrame = new GameFrame(this, msg.getSymbol(), msg.isYourTurn(), msg.getContent());
        gameFrame.setVisible(true);
    }

    // ====== Show screens ======
    private void showLobby() {
        lobbyFrame = new LobbyFrame(this);
        lobbyFrame.setVisible(true);
        requestLobbyUpdate();
    }

    public void showRegister() {
        registerFrame = new RegisterFrame(this);
        registerFrame.setVisible(true);
    }

    private void showLoginError(String err) {
        if (loginFrame == null) { loginFrame = new LoginFrame(this); loginFrame.setVisible(true); }
        loginFrame.showError(err);
    }

    private void showLobbyMessage(String msg) {
        if (lobbyFrame != null) lobbyFrame.showMessage(msg);
    }

    public void sendMessage(Message msg) {
        if (out != null) out.println(gson.toJson(msg));
    }

    public String getUsername() { return username; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CaroClient::new);
    }
}
