package websocket.messages;

import model.GameData;

public class NotificationMessage extends ServerMessage {
    private final GameData game;

    public NotificationMessage(GameData game) {
        super(ServerMessageType.NOTIFICATION);
        this.game = game;
    }
}
