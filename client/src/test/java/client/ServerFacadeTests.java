package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("localhost", String.valueOf(port));
    }

    @BeforeEach
    public void setUp() throws IOException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    // register success
    @Test
    public void testRegisterSuccess() throws Exception {
        AuthData authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.getAuthToken().length() > 10);
    }

    // register failure (duplicate registration)
    @Test
    public void testRegisterFailure() throws Exception {
        assertThrows(Exception.class, () -> facade.register("player1", "password", "p1@email.com"));
    }

    // login success
    @Test
    public void testLoginSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        AuthData authData = facade.login("player1", "password");
        assertTrue(authData.getAuthToken().length() > 10);
    }

    // login failure (not registered)
    @Test
    public void testLoginFailure() throws Exception {
        assertThrows(Exception.class, () -> facade.login("nonexistent", "password"));
    }

    // logout success
    @Test
    public void testLogoutSuccess() throws Exception {
        AuthData authData = facade.register("username", "password", "email");
        assertNotNull(authData);
        System.out.println(authData.getAuthToken());
        assertDoesNotThrow(() -> facade.logout(authData.getAuthToken()));
    }

    // logout failure (invalid auth token)
    @Test
    public void testLogoutFailure() throws Exception {
        AuthData authData = facade.register("username", "password", "email");
        assertThrows(Exception.class, () -> facade.logout("Invalid auth token"));
    }

    // create game success
    @Test
    public void testCreateGameSuccess() throws Exception {
        AuthData authData = facade.register("username", "password", "email");
        GameData gameData = facade.createGame(authData.getAuthToken(), "game1");
        assertEquals("game1", gameData.getGameName());
    }

    // create game failure (invalid auth token)
    @Test
    public void testCreateGameFailure() throws Exception {
        assertThrows(Exception.class, () -> facade.createGame("invalid token", "game1"));
    }

    // list games success
    @Test
    public void listGamesSuccess() throws Exception {
        AuthData authData = facade.register("player", "password", "email");
        GameData gameData = facade.createGame(authData.getAuthToken(), "chessgame");
        GameData[] games = facade.listGames(authData.getAuthToken());
        assertTrue(games.length > 0);
    }

    // list games failure (invalid auth token)
    @Test
    public void listGamesFailure() throws Exception {
        assertThrows(Exception.class, () -> facade.listGames("invalid auth"));
    }

    // join game success
    @Test
    public void joinGameSuccess() throws Exception {
        AuthData authData1 = facade.register("player1", "password", "email");
        AuthData authData2 = facade.register("player2", "password", "email");
        GameData gameData = facade.createGame(authData1.getAuthToken(), "Chess game");
        assertDoesNotThrow(() -> facade.joinGame(authData2.getAuthToken(), gameData.getGameID(), "White"));
    }

    // join game failure (invalid game name)
    @Test
    public void joinGameFailure() throws Exception {
        AuthData authData1 = facade.register("player1", "password", "email");
        GameData gameData = facade.createGame(authData1.getAuthToken(), "Chess game");
        assertThrows(Exception.class, () -> facade.joinGame("invalid auth", 10, "blue"));
    }
}
