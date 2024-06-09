package client;

import model.AuthData;
import model.GameData;
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
    private final Gson gson;

    public ServerFacade(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.gson = new Gson();
    }

    public AuthData register(String username, String password, String email) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/user");
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
            throw new IOException("Error registering user: " + connection.getResponseMessage());
        }
    }

    public AuthData login(String username, String password) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/session");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-type", "application/json");

        UserData userData = new UserData(username, password, null);
        String requestBody = gson.toJson(userData);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(new InputStreamReader(conn.getInputStream()), AuthData.class);
        } else {
            throw new IOException("Error logging in user: " + conn.getResponseMessage());
        }
    }

    public void logout(String authToken) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/session");
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("DELETE");
        connect.setRequestProperty("Authorization", "Bearer " + authToken);


        if (connect.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error logging out: " + connect.getResponseMessage());
        }
    }

    public GameData createGame(String authToken, String gameName) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/game");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + authToken);
        con.setRequestProperty("Content-type", "application/json");
        con.setDoOutput(true);

        GameData gameData = new GameData(0, null, null, gameName, null); // initialize with temporary game ID
        String requestBody = gson.toJson(gameData);

        try (OutputStream os = con.getOutputStream()) {
            os.write(requestBody.getBytes());
            os.flush();
        }

        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(new InputStreamReader(con.getInputStream()), GameData.class);
        } else {
            throw new IOException("Error creating game: " + con.getResponseMessage());
        }
    }

    public GameData[] listGames(String authToken) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/game");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer" + authToken);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return gson.fromJson(new InputStreamReader(connection.getInputStream()), GameData[].class);
        } else {
            throw new IOException("Error listing games: " + connection.getResponseMessage());
        }
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws IOException {
        URL url = new URL("http://" + serverHost + ":" + serverPort + "/game");
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("PUT");
        connect.setRequestProperty("Authorization", "Bearer" + authToken);
        connect.setRequestProperty("Content-type", "application/json");
        connect.setDoOutput(true);

        String requestBody = "{\"gameID\":" + gameID + ",\"playerColor\":\"" + playerColor + "\"}";

        try (OutputStream outs = connect.getOutputStream()) {
            outs.write(requestBody.getBytes());
            outs.flush();
        }

        if (connect.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error joining game: " + connect.getResponseMessage());
        }
    }

}
