<<<<<<< HEAD
package Model;

public class Message {
    private MessageType type;
    private String sender;
    private String content;
    private int x;
    private int y;
    private String symbol;      // "X" hoặc "O"
    private boolean isYourTurn; // Xác định lượt đánh công bằng

    public Message(MessageType type) {
        this.type = type;
    }

    // Toàn bộ Getter và Setter để thư viện Gson chuyển đổi dữ liệu tự động
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public boolean isYourTurn() { return isYourTurn; }
    public void setYourTurn(boolean yourTurn) { isYourTurn = yourTurn; }
}
=======
package Model;

import java.util.List;

public class Message {
    private MessageType type;
    private String sender;
    private String content;
    private int x;
    private int y;
    private String symbol;
    private boolean isYourTurn;

    // Auth fields
    private String username;
    private String password;

    // Room fields
    private String roomId;
    private String roomName;
    private List<Room> rooms;

    // Result
    private boolean success;
    private String errorMessage;

    public Message() {}
    public Message(MessageType type) { this.type = type; }

    // --- Getters/Setters ---
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public boolean isYourTurn() { return isYourTurn; }
    public void setYourTurn(boolean yourTurn) { isYourTurn = yourTurn; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
>>>>>>> main
