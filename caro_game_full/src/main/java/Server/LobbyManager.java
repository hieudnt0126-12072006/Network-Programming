package Server;

import Model.Message;
import Model.MessageType;
import Model.Room;
import java.util.List;

public class LobbyManager {

    public static void broadcastLobbyUpdate() {
        List<Room> rooms = RoomManager.getWaitingRooms();
        Message msg = new Message(MessageType.LOBBY_UPDATE);
        msg.setRooms(rooms);
        for (ClientHandler ch : ClientManager.getAllClients()) {
            if (ch.isInLobby()) {
                ch.sendMessage(msg);
            }
        }
    }
}
