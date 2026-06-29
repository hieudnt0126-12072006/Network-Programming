package Database;

public class DatabaseConfig {
    public static final String HOST = "localhost";
    public static final String INSTANCE = ""; // Ví dụ: "SQLEXPRESS" nếu dùng SQL Server Express
    public static final String DB_NAME = "caro_game";
    public static final String USERNAME = "sa";   // tên login SQL Server của bạn
    public static final String PASSWORD = "your_password"; // mật khẩu của bạn

    // Nếu dùng SQL Server Express: jdbc:sqlserver://localhost\\SQLEXPRESS;...
    // Nếu dùng SQL Server thường:  jdbc:sqlserver://localhost;...
    public static final String URL =
            "jdbc:sqlserver://localhost;databaseName=" + DB_NAME +
                    ";encrypt=false;trustServerCertificate=true;" +
                    "user=" + USERNAME + ";password=" + PASSWORD;
}