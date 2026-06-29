package util;

public class Validator {
    public static boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 3 && username.trim().length() <= 20
                && username.trim().matches("[a-zA-Z0-9_]+");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidRoomName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 30;
    }
}
