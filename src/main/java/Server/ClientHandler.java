package Server;

import Model.Message;
import Model.MessageType;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson = new Gson();

    private String username = "Ẩn danh";
    private String symbol;
    private ClientHandler opponent;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(socket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Message msg = gson.fromJson(inputLine, Message.class);
                if (msg == null) continue;

                switch (msg.getType()) {
                    case LOGIN:
                        this.username = msg.getSender();
                        this.opponent = null;
                        CaroServer.matchmaker(this);
                        break;

                    case MOVE:
                    case CHAT:
                        if (opponent != null) {
                            opponent.sendRaw(inputLine);
                        }
                        break;

                    case QUIT:
                        CaroServer.removeWaiting(this);
                        System.out.println("[HỦY] " + username + " hủy tìm trận.");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("[MẤT ĐƯỜNG TRUYỀN] Kết nối đóng đột ngột với: " + username);
        } finally {
            closeConnection();
        }
    }

    public void notifyStartGame(boolean isYourTurn) {
        Message startMsg = new Message(MessageType.START_GAME);
        startMsg.setSymbol(this.symbol);
        startMsg.setYourTurn(isYourTurn);
        startMsg.setContent(opponent != null ? opponent.getUsername() : "Đối thủ");
        sendRaw(gson.toJson(startMsg));
    }

    public void sendRaw(String rawData) {
        if (out != null) {
            out.println(rawData);
        }
    }

    public String getUsername() { return username; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setOpponent(ClientHandler opponent) { this.opponent = opponent; }

    private void closeConnection() {
        CaroServer.removeWaiting(this);
        try {
            if (opponent != null && opponent.socket != null && !opponent.socket.isClosed()) {
                Message quitMsg = new Message(MessageType.QUIT);
                quitMsg.setContent("Đối thủ rớt mạng, bạn đã thắng cuộc!");
                opponent.sendRaw(gson.toJson(quitMsg));
                opponent.setOpponent(null);
            }
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
