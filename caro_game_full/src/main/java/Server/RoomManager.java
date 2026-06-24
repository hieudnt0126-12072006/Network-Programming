package Server;

import Model.Room;
import Model.RoomStatus;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static int roomCounter = 1;

    public static synchronized Room createRoom(String roomName, String hostUsername) {
        String roomId = "ROOM-" + (roomCounter++);
        Room room = new Room(roomId, roomName, hostUsername);
        rooms.put(roomId, room);
        System.out.println("[LOBBY] Phòng tạo: " + roomName + " bởi " + hostUsername);
        return room;
    }

    public static synchronized Room joinRoom(String roomId, String guestUsername) {
        Room room = rooms.get(roomId);
        if (room == null) return null;
        if (room.isFull() || room.getStatus() != RoomStatus.WAITING) return null;
        room.setGuestUsername(guestUsername);
        room.setStatus(RoomStatus.PLAYING);
        return room;
    }

    public static synchronized void removeRoom(String roomId) {
        rooms.remove(roomId);
    }

    public static synchronized void setFinished(String roomId) {
        Room r = rooms.get(roomId);
        if (r != null) r.setStatus(RoomStatus.FINISHED);
    }

    public static List<Room> getWaitingRooms() {
        List<Room> list = new ArrayList<>();
        for (Room r : rooms.values())
            if (r.getStatus() == RoomStatus.WAITING) list.add(r);
        return list;
    }

    public static Room getRoom(String roomId) {
        return rooms.get(roomId);
    }
}
