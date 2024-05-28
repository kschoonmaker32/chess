package server;

import dataaccess.DataAccessException;
import service.UserService;
import model.UserData;
import model.AuthData;
import utils.JSONUtil;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserHandler {

    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    // register method
    public Route register = (Request req, Response res) -> {
        UserData user = JSONUtil.fromJson(req.body(), UserData.class);
        try {
            AuthData auth = userService.register(user);
            res.status(200);
            return JSONUtil.toJson(auth);
        } catch (DataAccessException e) {
            res.status(403);
            return "{\"message\" : \"Error: already taken\"}";
        }
    };

    // login method
    public Route login = (Request req, Response res) -> {
        UserData user = JSONUtil.fromJson(req.body(), UserData.class);
        try {
            AuthData auth = userService.login(user);
            res.status(200);
            return JSONUtil.toJson(auth);
        } catch (DataAccessException e ) {
            res.status(401);
            return "{\"message\" : \"Error: unauthorized\"}";
        }
    };

    // logout method
    public Route logout = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        try {
            userService.logout(authToken);
            res.status(200);
            return "{}";
        } catch (DataAccessException e ) {
            res.status(500);
            return "{\"message\" : \"Error:" + e.getMessage() + "\"}";
        }
    };
}
