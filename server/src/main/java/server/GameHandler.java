package server;

import dataaccess.DataAccessException;
import service.GameService;
import model.GameData;
import java.util.List;
import utils.JSONUtil;

import spark.Route;
import spark.Request;
import spark.Response;


public class GameHandler {

    private GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    // list games
    public Route listGames =  (Request req, Response res) -> {
        String authtoken = req.headers("Authorization");
        try {
            List<GameData> games = gameService.listGames(authtoken);
            res.status(200);
            return JSONUtil.toJson(games);
        } catch (DataAccessException e) {
            res.status(401);
            return "{\"message\" : \"Error: unauthorized\"}";
        }
    };

    // create games
    public Route createGame = (Request req, Response res) -> {
        String authtoken = req.headers("Authorization");
        String gameName = JSONUtil.fromJson(req.body(), String.class);
        try {
            GameData game = gameService.createGame(authtoken, gameName);
            res.status(200);
            return JSONUtil.toJson(game);
        } catch (DataAccessException e) {
            res.status(400);
            return "{\"message\" : \"Error: bad request\"}";
        }
    };

    // join game
    public Route joinGame = (Request req, Response res) -> {
        String authtoken = req.headers("Authorization");
        JoinGameRequest joinRequest = JSONUtil.fromJson(req.body(), JoinGameRequest.class);
        try {
            gameService.joinGame(authtoken, joinRequest.getGameID(), joinRequest.getPlayerColor());
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(403);
            return "{\"message\" : \"Error: already taken\"}";
        }
    };

    private static class JoinGameRequest {
        private String playerColor;
        private int gameID;

        public String getPlayerColor() {
            return playerColor;
        }

        public int getGameID() {
            return gameID;
        }
    }
}
