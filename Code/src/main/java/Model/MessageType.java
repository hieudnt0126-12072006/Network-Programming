<<<<<<< HEAD
package Model;

public enum MessageType {
    LOGIN,          // Gửi tên đăng nhập lên Server
    GET_LOBBY,      // Yêu cầu danh sách phòng/người chơi ở sảnh
    CREATE_ROOM,    // Yêu cầu tạo phòng đấu mới
    JOIN_ROOM,      // Yêu cầu vào một phòng có sẵn
    START_GAME,     // Server thông báo trận đấu bắt đầu
    MOVE,           // Đồng bộ tọa độ nước đi (X, Y)
    CHAT,           // Gửi tin nhắn chat
    LEFT_ROOM,      // Rời phòng đấu về lại sảnh
    QUIT            // Thoát hoàn toàn ứng dụng
}
=======
package Model;

public enum MessageType {
    // Auth
    REGISTER,
    LOGIN,
    LOGOUT,
    AUTH_SUCCESS,
    AUTH_FAIL,

    // Lobby
    GET_LOBBY,
    LOBBY_UPDATE,
    CREATE_ROOM,
    JOIN_ROOM,
    ROOM_CREATED,
    ROOM_JOINED,
    ROOM_FULL,
    ROOM_NOT_FOUND,
    LEFT_ROOM,

    // Game
    START_GAME,
    MOVE,
    MOVE_INVALID,
    GAME_OVER,
    CHAT,

    // System
    QUIT,
    ERROR,
    PONG
}
>>>>>>> main
