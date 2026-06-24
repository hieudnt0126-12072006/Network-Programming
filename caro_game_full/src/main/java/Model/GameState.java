package Model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String player1;   // X
    private String player2;   // O
    private String currentTurn; // username whose turn it is
    private List<Move> moves = new ArrayList<>();
    private boolean gameOver = false;
    private String winner;

    public GameState() {}
    public GameState(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentTurn = player1; // X goes first
    }

    public void addMove(Move m) { moves.add(m); }
    public int getMoveCount() { return moves.size(); }

    public String getPlayer1() { return player1; }
    public void setPlayer1(String player1) { this.player1 = player1; }
    public String getPlayer2() { return player2; }
    public void setPlayer2(String player2) { this.player2 = player2; }
    public String getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }
    public List<Move> getMoves() { return moves; }
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
}
