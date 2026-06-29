package Model;

public class Move {
    private int x;
    private int y;
    private String symbol;
    private String player;

    public Move() {}
    public Move(int x, int y, String symbol, String player) {
        this.x = x; this.y = y; this.symbol = symbol; this.player = player;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getPlayer() { return player; }
    public void setPlayer(String player) { this.player = player; }
}
