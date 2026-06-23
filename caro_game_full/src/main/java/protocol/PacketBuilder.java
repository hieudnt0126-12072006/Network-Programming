package protocol;

import Model.Message;
import Model.MessageType;

public class PacketBuilder {
    public static Message move(String sender, String symbol, int x, int y) {
        Message m = new Message(MessageType.MOVE);
        m.setSender(sender); m.setSymbol(symbol); m.setX(x); m.setY(y);
        return m;
    }

    public static Message chat(String sender, String content) {
        Message m = new Message(MessageType.CHAT);
        m.setSender(sender); m.setContent(content);
        return m;
    }

    public static Message login(String username, String password) {
        Message m = new Message(MessageType.LOGIN);
        m.setUsername(username); m.setPassword(password);
        return m;
    }
}
