package Game;

public class GameLogic {
    // Thuật toán quét 4 hướng (Ngang, dọc, 2 chéo) để tìm chuỗi 5 quân giống nhau
    public static boolean checkWin(Board board, int r, int c, String symbol) {
        int size = board.getSize();
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

        for (int[] dir : directions) {
            int count = 1;

            // Quét tiến về một hướng
            int i = r + dir[0];
            int j = c + dir[1];
            while (i >= 0 && i < size && j >= 0 && j < size && board.getCell(i, j).equals(symbol)) {
                count++;
                i += dir[0];
                j += dir[1];
            }

            // Quét lùi về hướng ngược lại
            i = r - dir[0];
            j = c - dir[1];
            while (i >= 0 && i < size && j >= 0 && j < size && board.getCell(i, j).equals(symbol)) {
                count++;
                i -= dir[0];
                j -= dir[1];
            }

            // Đạt từ 5 quân liên tiếp trở lên là thắng cuộc
            if (count >= Rule.WIN_LENGTH) {
                return true;
            }
        }
        return false;
    }
    public static boolean checkDraw(Board board){
        return board !=null && board.isFull();
    }

    public static GameResult checkGameResult(Board board,int lastRow,int lastCol,String symbol){
        if (board == null || !board.isValidPosition(lastRow, lastCol) || !Board.isValidSymbol(symbol) || !symbol.equals(board.getCell(lastRow, lastCol))){
        return GameResult.INVALID_MOVE;
        }
        if (checkWin(board, lastRow, lastCol, symbol)){
            return GameResult.WIN;
        }
        if (checkDraw(board)) {
            return GameResult.DRAW;
        }
        return GameResult.CONTINUE;
    }
}
