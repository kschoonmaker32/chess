package server;

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
        Spark.port(desiredPort);

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
        //GameHandler gameHandler = new GameHandler(gameService);

        // Register your endpoints and handle exceptions here.
        //Spark.get("/hi", (req, res) -> "kendra");
        Spark.post("/user", userHandler.register);

        Spark.awaitInitialization();
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
