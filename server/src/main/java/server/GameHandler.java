package server;

import dataaccess.DataAccessException;
import service.GameService;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import java.util.List;

import spark.Route;
import spark.Request;
import spark.Response;


public class GameHandler {

    private GameService gameService;
    private Gson gson = new Gson();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Route listGames =  (Request req, Response res) -> {
        String authtoken = req.headers("Authorization");
        try {
            List<GameData> games = gameService.listGames(authtoken);
            res.status(200);
            return gson.toJson(games);
        } catch (DataAccessException e) {
            res.status(401)
        }

    }

}
