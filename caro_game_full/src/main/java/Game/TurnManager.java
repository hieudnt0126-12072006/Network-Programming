package Game;

public class TurnManager {
    private String player1; // X
    private String player2; // O
    private String currentPlayer;

    public TurnManager(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    public boolean isCurrentPlayer(String username) {
        return currentPlayer.equals(username);
    }

    public void switchTurn() {
        currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
    }

    public String getCurrentPlayer() { return currentPlayer; }

    public String getSymbol(String username) {
        return username.equals(player1) ? "X" : "O";
    }
}
