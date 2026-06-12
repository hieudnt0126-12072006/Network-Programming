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