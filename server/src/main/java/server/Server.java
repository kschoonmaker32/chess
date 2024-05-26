package server;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;

import server.UserHandler;
import server.GameHandler;

import service.UserService;
import service.GameService;


import spark.*;

public class Server {

    public int run(int desiredPort) {
        // set server port
        Spark.port(desiredPort);

        // initialize static files
        Spark.staticFiles.location("web");

        // DAO instances
        UserDAO userDAO = new UserDAO();
        GameDAO gameDAO = new GameDAO();
        AuthDAO authDAO = new AuthDAO();

        // Service instances
        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO, authDAO);

        // Handler instances
        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", userHandler.register);
        Spark.post("/session", userHandler.login);
        Spark.delete("/session", userHandler.logout);
        Spark.get("/game", gameHandler.listGames);
        Spark.post("/game", gameHandler.createGame);
        Spark.put("/game", gameHandler.joinGame);

        // clear application
        Spark.delete("/db", (req, res) -> {
            try {
                userDAO.clear();
                gameDAO.clear();
                authDAO.clear();
                res.status(200);
                return "{}";
            } catch (Exception e) {
                res.status(400);
                return "{\"message\" : \"Error: " + e.getMessage()+ "\"}";
            }
        });


        Spark.awaitInitialization();

        // return port number
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) {
        new Server().run(8080);
    }
}
