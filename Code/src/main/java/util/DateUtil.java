package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String now() {
        return LocalDateTime.now().format(FORMATTER);
    }

    public static String format(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : "";
    }
}
