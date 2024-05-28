package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.GameData;
import java.util.List;
import java.util.Random;
import chess.ChessGame;

public class GameService {

    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clear()  {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        verifyAuth(authToken);
        return gameDAO.listGames();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        verifyAuth(authToken);
        int gameID = generateGameID();
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(game);
        return game;
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws  DataAccessException {
        verifyAuth(authToken);
        GameData game = gameDAO.getGame(gameID);
        if(gameDAO.getGame(gameID) == null) {
            throw new DataAccessException("Bad request");
        }
        if(playerColor == null) {
            throw new DataAccessException("No color listed");
        }
        if (playerColor.equalsIgnoreCase("WHITE") && game.getWhiteUsername() == null) {
            game.setWhiteUsername(getUsername(authToken));
        } else if (playerColor.equalsIgnoreCase("BLACK") && game.getBlackUsername() == null) {
            game.setBlackUsername(getUsername(authToken));
        } else if (game.getWhiteUsername() != null && game.getBlackUsername() != null) {
            throw new DataAccessException("Already taken");
        } else {
            throw new DataAccessException("Bad request");
    }
        gameDAO.updateGame(game);
    }

    public void verifyAuth(String authToken) throws DataAccessException {
        if(authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("Unauthorized");
        }
    }

    private String getUsername(String authToken) throws DataAccessException{
        return authDAO.getAuth(authToken).getUsername();
    }

    private int generateGameID() {
        return new Random().nextInt(10000);
    }
}
