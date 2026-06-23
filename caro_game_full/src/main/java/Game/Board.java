package Game;

public class Board {
    private int size;
    private String[][] matrix;

    public Board(int size) {
        this.size = size;
        this.matrix = new String[size][size];
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = "";
            }
        }
    }

    public boolean setMove(int x, int y, String symbol) {
        if (x >= 0 && x < size && y >= 0 && y < size && matrix[x][y].isEmpty()) {
            matrix[x][y] = symbol;
            return true;
        }
        return false;
    }

    public String getCell(int x, int y) {
        return matrix[x][y];
    }

    public int getSize() {
        return size;
    }
}