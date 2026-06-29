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
