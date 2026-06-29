<<<<<<< HEAD
package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CaroServer {
    private static final int PORT = 8080;
    // Danh sách hàng đợi những người chơi đang online chờ tìm trận đấu
    private static final List<ClientHandler> waitingPlayers = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("====== CARO SERVER ĐANG HOẠT ĐỘNG TRÊN CỔNG " + PORT + " ======");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[KẾT NỐI] Thiết bị mới kết nối: " + socket.getRemoteSocketAddress());

                ClientHandler handler = new ClientHandler(socket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("[LỖI SERVER] Khởi chạy cổng thất bại: " + e.getMessage());
        }
    }

    // Hệ thống ghép cặp tự động (Matchmaking)
    public static synchronized void matchmaker(ClientHandler player) {
        waitingPlayers.add(player);
        System.out.println("[HÀNG ĐỢI] " + player.getUsername() + " đang tìm đối thủ.");

        if (waitingPlayers.size() >= 2) {
            ClientHandler p1 = waitingPlayers.remove(0);
            ClientHandler p2 = waitingPlayers.remove(0);

            // Gán đối thủ trực tiếp cho nhau
            p1.setOpponent(p2);
            p2.setOpponent(p1);

            p1.setSymbol("X");
            p2.setSymbol("O");

            // Phát lệnh khởi động giao diện bàn cờ chiến đấu cho cả 2 máy Client
            p1.notifyStartGame(true);  // X đi trước
            p2.notifyStartGame(false); // O đi sau

            System.out.println("[TRẬN ĐẤU] Đã ghép trận đấu thành công: " + p1.getUsername() + " vs " + p2.getUsername());
        }
    }

    public static void removeWaiting(ClientHandler player) {
        waitingPlayers.remove(player);
    }
}
=======
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
>>>>>>> main
