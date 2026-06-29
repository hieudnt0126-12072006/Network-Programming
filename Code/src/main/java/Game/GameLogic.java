<<<<<<< HEAD
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
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }
=======
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
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }
>>>>>>> main
}