package server;

import com.google.gson.Gson;
import spark.Session;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class WebSocketHandler {
    private static final Map<Integer, Set<Session>> gameSessions = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();


    public void onOpen(Session session) {
        //
    }

    public void onMessage(String message, Session session) throws IOException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthString();

        switch (command.getCommandType()) {
            case CONNECT:
                ConnectCommand connectCommand = gson.fromJson(message, ConnectCommand.class);
        }
    }

    public void
}
