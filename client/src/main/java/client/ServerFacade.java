package client;

import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class ServerFacade {

    private final String serverHost;
    private final String serverPort;
    private Gson gson;

    public ServerFacade(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;


    }
}
