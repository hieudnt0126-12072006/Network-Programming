package Model;

public class Room {
    private String roomId;
    private String roomName;
    private String hostUsername;
    private String guestUsername;
    private RoomStatus status;

    public Room() {}
    public Room(String roomId, String roomName, String hostUsername) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.hostUsername = hostUsername;
        this.status = RoomStatus.WAITING;
    }

    public boolean isFull() { return guestUsername != null; }
    public boolean isEmpty() { return hostUsername == null; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public String getHostUsername() { return hostUsername; }
    public void setHostUsername(String hostUsername) { this.hostUsername = hostUsername; }
    public String getGuestUsername() { return guestUsername; }
    public void setGuestUsername(String guestUsername) { this.guestUsername = guestUsername; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}
