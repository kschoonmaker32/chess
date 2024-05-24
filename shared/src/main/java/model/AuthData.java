package model;

public class AuthData {
    private String authToken;
    private String username;

    //constructor to create auth data
    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    //GETTERS
    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    //SETTERS
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername() {
        this.username = username;
    }
}
