package Game;

// Wrapper delegate sang GameLogic
public class WinChecker {
    public static boolean hasWon(Board board, int r, int c, String symbol) {
        return GameLogic.checkWin(board, r, c, symbol);
    }

    public static boolean isBoardFull(Board board) {
        int size = board.getSize();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board.getCell(i, j).isEmpty()) return false;
        return true;
    }
}
