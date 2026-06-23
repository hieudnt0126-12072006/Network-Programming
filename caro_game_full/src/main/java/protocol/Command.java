package protocol;

// Các lệnh dùng trong giao tiếp - hiện dùng MessageType enum thay thế
public class Command {
    public static final String LOGIN      = "LOGIN";
    public static final String REGISTER   = "REGISTER";
    public static final String CREATE_ROOM = "CREATE_ROOM";
    public static final String JOIN_ROOM  = "JOIN_ROOM";
    public static final String MOVE       = "MOVE";
    public static final String CHAT       = "CHAT";
    public static final String QUIT       = "QUIT";
}
