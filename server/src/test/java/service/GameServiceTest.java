package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import model.GameData;
import model.AuthData;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GameServiceTest {
    private GameService gameService;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() {
        gameDAO = new GameDAO();
        authDAO = new AuthDAO();
        gameService = new GameService(gameDAO, authDAO);
    }

    // list games success
    @Test
    public void testListGamesSuccess() throws Exception {
        AuthData auth = new AuthData("authToken", "testUser");
        authDAO.createAuth(auth);

        gameService.createGame(auth.getAuthToken(), "ChessGame 1");
        List<GameData> games = gameService.listGames(auth.getAuthToken());

        assertNotNull(games);
        assertFalse(games.isEmpty());
    }

    // list games failure (unauthorized)
    @Test
    public void testListGamesFailure() throws Exception {
        AuthData auth = new AuthData("authToken", "testUser");
        authDAO.createAuth(auth);

        gameService.createGame(auth.getAuthToken(), "ChessGame 1");
        assertThrows(DataAccessException.class, () -> gameService.listGames("invalid token"));
    }

    // create game success
    @Test
    public void testCreateGameSuccess() throws Exception {
        AuthData auth = new AuthData("authToken", "testUser");
        authDAO.createAuth(auth);

        GameData game = gameService.createGame(auth.getAuthToken(), "ChessGame 1");
        assertEquals("ChessGame 1", game.getGameName());
    }

    // create game failure (unauthorized)
    @Test
    public void testCreateGameFailure() throws Exception {
        AuthData auth = new AuthData("authToken", "testUser");
        authDAO.createAuth(auth);

        assertThrows(DataAccessException.class, () -> gameService.createGame("invalid token", "ChessGame 1"));
    }

    // join game success
    @Test
    public void testJoinGameSuccess() throws Exception {
        AuthData auth = new AuthData("authToken", "testUser");
        authDAO.createAuth(auth);

        GameData game = gameService.createGame(auth.getAuthToken(), "ChessGame 1");
        gameService.joinGame(auth.getAuthToken(), game.getGameID(), "WHITE");

        GameData updatedGame = gameDAO.getGame(game.getGameID());
        assertEquals("testUser", updatedGame.getWhiteUsername());
    }

    // join game failure (game not found)
    @Test
    public void testJoinGameFailure() throws Exception {
        AuthData auth = new AuthData("authToken", "testUser");
        authDAO.createAuth(auth);

        assertThrows(DataAccessException.class, () -> gameService.joinGame(auth.getAuthToken(), 7, "WHITE"));
    }

}


