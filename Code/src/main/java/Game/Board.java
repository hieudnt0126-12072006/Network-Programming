package Game;

public class Board {
    public static final String EMPTY_CELL = "";
    private final int size;
    private final String[][] matrix;
    private int moveCount;

    public boolen isFull(){
        return moveCount >=size + size;
    }

    public Board(int size) {
        this.size = size;
        this.matrix = new String[size][size];
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = EMPTY_CELL;
            }
        }
        moveCount = 0;
    }

    public boolean setMove(int x, int y, String symbol) {
        if (!isValidPosition(x,y) || !isCellEmpty(x,y)) {
            return false;
        }
        matrix[x][y] = symbol;
        moveCount++;
        return true;
    }

    public boolean isValidPosition(int x,int y){
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public boolean isCellEmpty(int x,int y){
        return isValidPosition(x,y) && matrix[x][y].isEmpty();
    }

    public boolean isValidSymbol(String symbol){
            return PlayerSymbol.isValid(symbol);
    }
    
    public String getCell(int x, int y) {
        if (!isValidPosition(x,y)) {
             return matrix[x][y];
        }
    }

    public int getSize() {
        return size;
    }
}
