package Server;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private static final ConcurrentHashMap<String, ClientHandler> onlineClients = new ConcurrentHashMap<>();

    public static void register(String username, ClientHandler handler) {
        onlineClients.put(username, handler);
        System.out.println("[ONLINE] " + username + " đã đăng nhập. Tổng: " + onlineClients.size());
    }

    public static void unregister(String username) {
        onlineClients.remove(username);
        System.out.println("[OFFLINE] " + username + " đã thoát. Tổng: " + onlineClients.size());
    }

    public static ClientHandler getClient(String username) {
        return onlineClients.get(username);
    }

    public static boolean isOnline(String username) {
        return onlineClients.containsKey(username);
    }

    public static Collection<ClientHandler> getAllClients() {
        return onlineClients.values();
    }

    public static int getOnlineCount() {
        return onlineClients.size();
    }
}
