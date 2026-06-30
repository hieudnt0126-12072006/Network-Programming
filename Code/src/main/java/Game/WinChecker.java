package Game;

public class WinChecker {
    public static boolean hasWon(Board board, int r, int c, String symbol) {
        return GameLogic.checkWin(board, r, c, symbol);
    }
    public static boolean isBoardFull(Board board) {
        return board.isFull();
    }
}
