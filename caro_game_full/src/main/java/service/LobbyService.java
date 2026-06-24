package service;

import Model.Room;
import Server.RoomManager;
import java.util.List;

public class LobbyService {
    public List<Room> getWaitingRooms() {
        return RoomManager.getWaitingRooms();
    }
}
