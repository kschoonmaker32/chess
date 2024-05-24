package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.GameData;
import model.AuthData;
import java.util.List;
import java.util.Random;
import chess.ChessGame;

public class GameService {

    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public List<GameData> listGames(String AuthToken) throws DataAccessException {
        verifyAuth(AuthToken);
        return gameDAO.listGames();
    }

    public GameData createGame(String AuthToken, String gameName) throws DataAccessException {
        verifyAuth(AuthToken);
        int gameID = generateGameID();
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(game);
        return game;
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws  DataAccessException {
        verifyAuth(authToken);
        GameData game = gameDAO.getGame(gameID);
        if(gameDAO.getGame(gameID) == null) {
            throw new DataAccessException("Game not found.");
        }
        if (playerColor.equalsIgnoreCase("WHITE")) {
            if (game.getWhiteUsername() != null) {
                throw new DataAccessException("White player already taken.");
            }
            game.setWhiteUsername(getUsername(authToken));
        } else if (playerColor.equalsIgnoreCase("BLACK")) {
            if (game.getWhiteUsername() != null) {
                throw new DataAccessException("Black player already taken.");
            }
            game.setBlackUsername(getUsername(authToken));
        }
        gameDAO.updateGame(game);
    }

    public void verifyAuth(String AuthToken) throws DataAccessException {
        if(authDAO.getAuth(AuthToken) == null) {
            throw new DataAccessException("Unauthorized.");
        }
    }

    private String getUsername(String authToken) throws DataAccessException{
        return authDAO.getAuth(authToken).getUsername();
    }

    private int generateGameID() {
        return new Random().nextInt(10000); // find out how this works
    }
}
