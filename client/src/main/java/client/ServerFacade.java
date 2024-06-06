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

    public AuthData register(String username, String password, String email) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-type", "application/json");

        UserData userData = new UserData(username, password, email);
        String requestBody = gson.toJson(userData);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes());
            os.flush();
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(new InputStreamReader(connection.getInputStream()), AuthData.class);
        } else {
            throw new IOException("Error registering user" + connection.getResponseMessage());
        }
    }
}
