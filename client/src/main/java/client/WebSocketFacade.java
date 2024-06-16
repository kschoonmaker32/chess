package client;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint
public class WebSocketFacade {
    private Session session;
    private final Gson gson;

    public WebSocketFacade(String serverURI) throws URISyntaxException, DeploymentException, IOException {
        this.gson = new Gson();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, new URI(serverURI));
        } catch (Exception e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            throw e;
        }
    }

    public void sendConnect(String authToken, int gameID) throws IOException {
        ConnectCommand connectCommand = new ConnectCommand(authToken, gameID);
        sendMessage(connectCommand);
    }

    public void sendLeave(String authToken, int gameID) throws IOException {
        LeaveCommand leaveCommand = new LeaveCommand(authToken, gameID);
        sendMessage(leaveCommand);
    }

    public void sendMakeMove(String authToken, int gameID, ChessMove move) throws IOException {
        MakeMoveCommand moveCommand = new MakeMoveCommand(authToken, gameID, move);
        sendMessage(moveCommand);
    }

    public void sendResign(String authToken, int gameID) throws IOException {
        ResignCommand resignCommand = new ResignCommand(authToken, gameID);
        sendMessage(resignCommand);
    }

    private void sendMessage(UserGameCommand command) throws IOException{
        String message = gson.toJson(command);
        session.getBasicRemote().sendText(message);
    }
}
