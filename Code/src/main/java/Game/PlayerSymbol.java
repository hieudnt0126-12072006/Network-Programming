package Game;

public enum PlayerSymnol {
  X("X"), O("O");
  private final String value;
  PlayerSymbol(String value){this.value = value;}
  public String getValue() {return value;}
  public static boolean isValid(String symbol){
    return X.value.equals(symbol) || O.value.equals(symbol);
  }
}
