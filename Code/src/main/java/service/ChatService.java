package service;

// Chat được xử lý trực tiếp qua ClientHandler relay - không cần service riêng.
// Class này dự phòng cho log/filter sau này.
public class ChatService {
    public String filter(String message) {
        if (message == null) return "";
        return message.trim().substring(0, Math.min(message.trim().length(), 200));
    }
}
