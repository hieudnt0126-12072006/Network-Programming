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

    public CaroClient() {
        loginFrame = new LoginFrame(this);
        loginFrame.setVisible(true);
    }

    public void startConnection(String name) {
        this.username = name;
        try {
            socket = new Socket(SERVER_IP, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            Message loginMsg = new Message(MessageType.LOGIN);
            loginMsg.setSender(username);
            sendMessage(loginMsg);

            loginFrame.setVisible(false);
            showLobby();

            new Thread(this::listenServer).start();

        } catch (Exception e) {
            loginFrame.showError("Không kết nối được đến Server. Vui lòng mở CaroServer trước!");
        }
    }

    public void requestRematch() {
        SwingUtilities.invokeLater(() -> {
            if (gameFrame != null) { gameFrame.dispose(); gameFrame = null; }
            showLobby();
        });
        Message loginMsg = new Message(MessageType.LOGIN);
        loginMsg.setSender(username);
        sendMessage(loginMsg);
    }

    public void cancelMatchmaking() {
        Message quitMsg = new Message(MessageType.QUIT);
        quitMsg.setSender(username);
        sendMessage(quitMsg);

        SwingUtilities.invokeLater(() -> {
            if (lobbyFrame != null) { lobbyFrame.dispose(); lobbyFrame = null; }
            loginFrame.showError("");
            loginFrame.setVisible(true);
        });
    }

    private void showLobby() {
        lobbyFrame = new LobbyFrame(e -> cancelMatchmaking());
        lobbyFrame.setVisible(true);
    }

    private void listenServer() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                Message msg = gson.fromJson(line, Message.class);
                if (msg == null) continue;

                switch (msg.getType()) {
                    case START_GAME:
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
                        if (gameFrame != null) {
                            final int x = msg.getX();
                            final int y = msg.getY();
                            final String sym = msg.getSymbol();
                            SwingUtilities.invokeLater(() -> gameFrame.updateOpponentMove(x, y, sym));
                        }
                        break;

                    case CHAT:
                        if (gameFrame != null) {
                            final String chatLine = msg.getSender() + ": " + msg.getContent();
                            SwingUtilities.invokeLater(() -> gameFrame.appendChat(chatLine));
                        }
                        break;

                    case QUIT:
                        if (gameFrame != null) {
                            final String content = msg.getContent();
                            SwingUtilities.invokeLater(() -> gameFrame.handleOpponentLeft(content));
                        }
                        break;
                }
            }
        } catch (IOException e) {
            if (gameFrame != null) {
                SwingUtilities.invokeLater(() -> gameFrame.appendChat("[HỆ THỐNG] Mất kết nối đến máy chủ."));
            }
        }
    }

    public void sendMessage(Message msg) {
        if (out != null) {
            out.println(gson.toJson(msg));
        }
    }

    public String getUsername() { return username; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CaroClient::new);
    }
}
