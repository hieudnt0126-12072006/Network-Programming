package protocol;

import Model.Message;
import com.google.gson.Gson;

public class PacketParser {
    private static final Gson GSON = new Gson();

    public static Message parse(String json) {
        try { return GSON.fromJson(json, Message.class); }
        catch (Exception e) { return null; }
    }

    public static String serialize(Message msg) {
        return GSON.toJson(msg);
    }
}
