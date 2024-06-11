package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;


import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.GameService;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

public class WebSocketHandler {
    private static final Map<Integer, Set<Session>> gameSessions = new ConcurrentHashMap<>();
    private static final Gson gson = new Gson();
    private final GameService gameService;

    public WebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }


    public void onOpen(Session session) {
        //
    }

    @OnWebSocketMessage
    public void onMessage(String message, Session session) throws IOException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthString();

        switch (command.getCommandType()) {
            case CONNECT:
                ConnectCommand connectCommand = gson.fromJson(message, ConnectCommand.class);
                handleConnect(session, authToken, connectCommand.getGameID());
                break;
            case LEAVE:
                LeaveCommand leaveCommand = gson.fromJson(message, LeaveCommand.class);
                handleLeave(session, authToken, leaveCommand.getGameID());
                break;
        }
    }

    private void handleConnect(Session session, String authToken, int gameID) throws IOException {
        gameSessions.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet()).add(session);
        sendNotificationToOthers(gameID, session, authToken + " connected to game " + gameID);
        sendLoadGameMessage(gameID, session);
    }

    private void handleLeave(Session session, String authToken, int gameID) throws IOException {
        Set<Session> sessions = gameSessions.get(gameID);
        if (sessions != null) {
            sessions.remove(session);
            sendNotificationToOthers(gameID, session, authToken + " left the game");
        }
    }

    private void sendLoadGameMessage(int gameID, Session session) throws IOException {
        try {
            GameData gameData = gameService.getGameData(gameID);
            LoadGameMessage loadGameMessage = new LoadGameMessage(gameData);
            String message = gson.toJson(loadGameMessage);
            session.getRemote().sendString(message);
        } catch (DataAccessException e) {
            sendError(session, "Failed to load game data");
        }
    }

    private void sendNotificationToOthers(int gameID, Session senderSession, String notification) throws IOException {
        NotificationMessage notificationMessage = new NotificationMessage(notification);
        String message = gson.toJson(notificationMessage);

        sendToOthers(gameID, senderSession, message);
    }

    private void sendToOthers(int gameID, Session senderSession, String message) throws IOException {
        Set<Session> sessions = gameSessions.get(gameID);
        if (sessions != null) {
            for (Session session : sessions) {
                if (!session.equals(senderSession)) {
                    session.getRemote().sendString(message);
                }
            }
        }
    }

    private void sendNotificationToAll(int gameID, String notification) throws IOException {
        NotificationMessage notificationMessage = new NotificationMessage(notification);
        String message = gson.toJson(notificationMessage);

        sendToAll(gameID, message);
    }

    private void sendToAll(int gameID, String message) throws IOException {
        Set<Session> sessions = gameSessions.get(gameID);
        if(sessions != null) {
            for (Session session : sessions) {
                session.getRemote().sendString(message);
            }
        }
    }

    private void sendError(Session session, String errorMessage) throws IOException {
        ErrorMessage errorMessageObj = new ErrorMessage(errorMessage);
        String message = gson.toJson(errorMessageObj);
        session.getRemote().sendString(message);
    }

}
