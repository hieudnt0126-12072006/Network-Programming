package Game;
public class Cell {
    private int row;
    private int col;
    private String symbol;
    public Cell(int row, int col, String symbol) {
        this.row = row; this.col = col; this.symbol = symbol;
    }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getSymbol() { return symbol; }
    public boolean isEmpty() { return symbol == null || symbol.isEmpty(); }
}
