package client;

import com.google.gson.Gson;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;

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

    private void sendMessage(UserGameCommand command) throws IOException{
        String message = gson.toJson(command);
        session.getBasicRemote().sendText(message);
    }

    public void receiveMessage() throws InterruptedException {
        messageQueue.take();
    }
}
