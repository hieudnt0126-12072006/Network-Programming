package service;

import Model.Room;
import Model.RoomStatus;
import Server.RoomManager;
import java.util.List;

public class RoomService {
    public Room createRoom(String roomName, String hostUsername) {
        return RoomManager.createRoom(roomName, hostUsername);
    }

    public Room joinRoom(String roomId, String guestUsername) {
        return RoomManager.joinRoom(roomId, guestUsername);
    }

    public List<Room> getAvailableRooms() {
        return RoomManager.getWaitingRooms();
    }
}
