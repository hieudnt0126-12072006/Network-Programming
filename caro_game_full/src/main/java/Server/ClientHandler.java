package Server;

import Model.Message;
import Model.MessageType;
import Model.Room;
import Model.User;
import com.google.gson.Gson;
import service.AuthenticationService;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final Gson gson = new Gson();

    private String username;
    private boolean inLobby = false;
    private String currentRoomId;
    private MatchManager matchManager;

    private static final AuthenticationService authService = new AuthenticationService();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

            String line;
            while ((line = in.readLine()) != null) {
                Message msg = gson.fromJson(line, Message.class);
                if (msg == null) continue;
                dispatch(msg, line);
            }
        } catch (IOException e) {
            System.out.println("[DISCONNECT] " + (username != null ? username : "Unknown"));
        } finally {
            cleanup();
        }
    }

    private void dispatch(Message msg, String rawLine) {
        switch (msg.getType()) {
            case REGISTER -> handleRegister(msg);
            case LOGIN    -> handleLogin(msg);
            case GET_LOBBY  -> handleGetLobby();
            case CREATE_ROOM -> handleCreateRoom(msg);
            case JOIN_ROOM   -> handleJoinRoom(msg);
            case LEFT_ROOM   -> handleLeftRoom();
            case MOVE  -> { if (matchManager != null) matchManager.handleMove(this, msg.getX(), msg.getY()); }
            case CHAT  -> relayChatToOpponent(rawLine);
            case QUIT  -> { /* handled by cleanup */ }
            default -> {}
        }
    }

    // ==================== AUTH ====================
    private void handleRegister(Message msg) {
        String error = authService.register(msg.getUsername(), msg.getPassword());
        Message resp = new Message(MessageType.AUTH_SUCCESS);
        if (error != null) {
            resp.setType(MessageType.AUTH_FAIL);
            resp.setErrorMessage(error);
        } else {
            resp.setContent("Đăng ký thành công! Vui lòng đăng nhập.");
        }
        sendMessage(resp);
    }

    private void handleLogin(Message msg) {
        User user = authService.login(msg.getUsername(), msg.getPassword());
        if (user == null) {
            Message fail = new Message(MessageType.AUTH_FAIL);
            fail.setErrorMessage("Sai tên đăng nhập hoặc mật khẩu!");
            sendMessage(fail);
            return;
        }
        if (ClientManager.isOnline(user.getUsername())) {
            Message fail = new Message(MessageType.AUTH_FAIL);
            fail.setErrorMessage("Tài khoản đang đăng nhập từ nơi khác!");
            sendMessage(fail);
            return;
        }
        this.username = user.getUsername();
        ClientManager.register(username, this);
        this.inLobby = true;

        Message ok = new Message(MessageType.AUTH_SUCCESS);
        ok.setSender(username);
        ok.setContent("Chào mừng " + username + "! Wins: " + user.getWins() + " | Losses: " + user.getLosses());
        sendMessage(ok);
        handleGetLobby();
    }

    // ==================== LOBBY ====================
    private void handleGetLobby() {
        List<Room> rooms = RoomManager.getWaitingRooms();
        Message resp = new Message(MessageType.LOBBY_UPDATE);
        resp.setRooms(rooms);
        sendMessage(resp);
    }

    private void handleCreateRoom(Message msg) {
        if (username == null) return;
        String roomName = msg.getRoomName();
        if (roomName == null || roomName.trim().isEmpty()) roomName = username + "'s Room";
        Room room = RoomManager.createRoom(roomName.trim(), username);
        this.currentRoomId = room.getRoomId();
        this.inLobby = false;

        Message resp = new Message(MessageType.ROOM_CREATED);
        resp.setRoomId(room.getRoomId());
        resp.setRoomName(room.getRoomName());
        resp.setContent("Phòng \"" + room.getRoomName() + "\" đã được tạo. Đang chờ đối thủ...");
        sendMessage(resp);
        LobbyManager.broadcastLobbyUpdate();
    }

    private void handleJoinRoom(Message msg) {
        if (username == null) return;
        Room room = RoomManager.joinRoom(msg.getRoomId(), username);
        if (room == null) {
            Message fail = new Message(MessageType.ROOM_FULL);
            fail.setContent("Phòng đã đầy hoặc không tồn tại!");
            sendMessage(fail);
            return;
        }
        this.currentRoomId = room.getRoomId();
        this.inLobby = false;

        ClientHandler hostHandler = ClientManager.getClient(room.getHostUsername());
        if (hostHandler == null) {
            Message fail = new Message(MessageType.ERROR);
            fail.setContent("Chủ phòng đã thoát.");
            sendMessage(fail);
            RoomManager.removeRoom(room.getRoomId());
            return;
        }

        // Tạo MatchManager
        MatchManager mm = new MatchManager(room.getRoomId(), hostHandler, this);
        hostHandler.setMatchManager(mm);
        this.setMatchManager(mm);

        // Thông báo bắt đầu
        Message startHost = new Message(MessageType.START_GAME);
        startHost.setSymbol("X");
        startHost.setYourTurn(true);
        startHost.setContent(username);
        hostHandler.sendMessage(startHost);

        Message startGuest = new Message(MessageType.START_GAME);
        startGuest.setSymbol("O");
        startGuest.setYourTurn(false);
        startGuest.setContent(room.getHostUsername());
        sendMessage(startGuest);

        LobbyManager.broadcastLobbyUpdate();
        System.out.println("[TRẬN ĐẤU] " + room.getHostUsername() + " (X) vs " + username + " (O) | Phòng: " + room.getRoomId());
    }

    private void handleLeftRoom() {
        if (currentRoomId != null) {
            RoomManager.removeRoom(currentRoomId);
            currentRoomId = null;
        }
        inLobby = true;
        handleGetLobby();
        LobbyManager.broadcastLobbyUpdate();
    }

    // ==================== CHAT ====================
    private void relayChatToOpponent(String rawLine) {
        if (matchManager == null) return;
        // Tìm opponent qua currentRoomId
        Room room = currentRoomId != null ? RoomManager.getRoom(currentRoomId) : null;
        if (room == null) return;
        String oppName = username.equals(room.getHostUsername()) ? room.getGuestUsername() : room.getHostUsername();
        if (oppName == null) return;
        ClientHandler opp = ClientManager.getClient(oppName);
        if (opp != null) opp.sendRaw(rawLine);
    }

    // ==================== UTILS ====================
    public void sendMessage(Message msg) {
        sendRaw(gson.toJson(msg));
    }

    public void sendRaw(String raw) {
        if (out != null) out.println(raw);
    }

    private void cleanup() {
        if (matchManager != null) {
            matchManager.handlePlayerDisconnect(this);
            matchManager = null;
        }
        if (currentRoomId != null) {
            RoomManager.removeRoom(currentRoomId);
            currentRoomId = null;
        }
        if (username != null) {
            ClientManager.unregister(username);
            LobbyManager.broadcastLobbyUpdate();
        }
        try { if (socket != null) socket.close(); } catch (IOException ignored) {}
    }

    public String getUsername() { return username; }
    public boolean isInLobby() { return inLobby; }
    public void setMatchManager(MatchManager mm) { this.matchManager = mm; }
}
