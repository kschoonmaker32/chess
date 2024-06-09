package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

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

    //@BeforeEach
    // clear database somehow

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
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
        AuthData authData = facade.register("player1", "password", "p1@email.com");
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
        AuthData authData = facade.login("username", "password");
        assertDoesNotThrow(() -> facade.logout(authData.getAuthToken()));
    }

    @Test
    public void testLogoutFailure() throws Exception {
        AuthData authData = facade.login("username", "password");
        assertThrows(Exception.class, () -> facade.logout("Invalid auth token"));
    }


}
