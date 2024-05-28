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

//    // register method
//    public Route register = (Request req, Response res) -> {
//        UserData user = JSONUtil.fromJson(req.body(), UserData.class);
//        try {
//            AuthData auth = userService.register(user);
//            res.status(200);
//            return JSONUtil.toJson(auth);
//        } catch (InvalidUserData e) {
//
//        }
//
//        catch (DataAccessException e) {
//            res.status(403);
//            return "{\"message\" : \"Error: already taken\"}";
//        }
//    };


    public Route register = (Request req, Response res) -> {
        try {
            UserData user = JSONUtil.fromJson(req.body(), UserData.class);
            validateUserData(user); // Validate user data

            AuthData auth = userService.register(user);
            res.status(200);
            return JSONUtil.toJson(auth);
        } catch (InvalidUserDataException e) {
            res.status(400);
            return "{\"message\" : \"Error: bad request\"}";
        } catch (DataAccessException e) {
            res.status(403);
            return "{\"message\" : \"Error: already taken\"}";
        } catch (Exception e) {
            res.status(500);
            return "{\"message\" : \"Error: " + e.getMessage() + "\"}";
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


        //continue editing route methods until they have all the error codes neccesary. youre so close you got this!! :)
    };

    // logout method
    public Route logout = (Request req, Response res) -> {
        String authToken = req.headers("Authorization");
        try {
            validateAuthToken(authToken);
            userService.logout(authToken);
            res.status(200);
            return "{}";
        } catch(InvalidUserDataException e) {
            res.status(401);
            return "{\"message\" : \"Error: unauthorized\"}";
        } catch (DataAccessException e ) {
            res.status(401);
            return "{\"message\" : \"Error:" + e.getMessage() + "\"}";
        }
    };

    private void validateUserData(UserData user) throws Exception {
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
            user.getPassword() == null || user.getPassword().isEmpty() ||
            user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidUserDataException("Invalid user data");
        }
    }

    private void validateAuthToken(String authToken) throws Exception {
        if(authToken == null || authToken.isEmpty()) {
            throw new InvalidUserDataException("Invalid auth toekn");
        }
    }

    class InvalidUserDataException extends Exception {
        public InvalidUserDataException(String message) {
            super(message);
        }
    }
}
