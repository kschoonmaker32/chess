package client;

import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class WSFacade {
    private Session session;
    private final Gson gson;
    private final BlockingDeque<String> messageQueue;

    public WSFacade(String serverURI) throws URISyntaxException, DeploymentException, IOException {
        this.gson = new Gson();
        this.messageQueue = new LinkedBlockingDeque<>();
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(serverURI));
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        messageQueue.offer(message);
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose() {
        this.session = null;
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

    public void receiveMessage() throws InterruptedException {
        messageQueue.take();
    }
}
