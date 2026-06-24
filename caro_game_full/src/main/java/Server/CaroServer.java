package Server;

import Database.DatabaseInitializer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CaroServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("====== CARO SERVER KHỞI ĐỘNG TRÊN CỔNG " + PORT + " ======");

        // Khởi tạo database
        try {
            DatabaseInitializer.initialize();
        } catch (Exception e) {
            System.err.println("[CẢNH BÁO] Không kết nối được MySQL: " + e.getMessage());
            System.err.println("[CẢNH BÁO] Server chạy nhưng KHÔNG có tính năng đăng nhập/lưu lịch sử.");
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[READY] Đang lắng nghe kết nối...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[KẾT NỐI] " + socket.getRemoteSocketAddress());
                ServerThreadPool.submit(new ClientHandler(socket));
            }
        } catch (IOException e) {
            System.err.println("[LỖI SERVER] " + e.getMessage());
        }
    }
}
