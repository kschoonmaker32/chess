package dataaccess;

import model.GameData;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    //keep track of games
    private Map<Integer, GameData> games = new HashMap<>();

    public void clear() {
        games.clear();
    }

    public void createGame(GameData game) throws DataAccessException{
        if(games.containsKey(game.getGameID())) {
            throw new DataAccessException("This game already exists.");
        }
        games.put(game.getGameID(), game);
    }

    public GameData getGame(int gameID) throws DataAccessException{
        if(!games.containsKey(gameID)) {
            throw new DataAccessException("Game not found.");
        }
        return games.get(gameID);
    }

    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    public void updateGame(GameData game) throws DataAccessException{
        if(!games.containsKey(game.getGameID())) {
            throw new DataAccessException("Game not found.");
        }
        games.put(game.getGameID(), game);
    }
}
