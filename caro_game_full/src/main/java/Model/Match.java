package Model;

public class Match {
    private int id;
    private String player1;
    private String player2;
    private String winner;  // null = draw
    private String playedAt;
    private int totalMoves;

    public Match() {}
    public Match(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPlayer1() { return player1; }
    public void setPlayer1(String player1) { this.player1 = player1; }
    public String getPlayer2() { return player2; }
    public void setPlayer2(String player2) { this.player2 = player2; }
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
    public String getPlayedAt() { return playedAt; }
    public void setPlayedAt(String playedAt) { this.playedAt = playedAt; }
    public int getTotalMoves() { return totalMoves; }
    public void setTotalMoves(int totalMoves) { this.totalMoves = totalMoves; }
}
