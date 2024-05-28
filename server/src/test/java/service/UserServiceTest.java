package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.UserData;
import model.AuthData;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAO();
        authDAO = new AuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    // register success
    @Test
    public void testRegisterSuccess() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        AuthData auth = userService.register(user);

        assertNotNull(auth);
        assertEquals("testUser", auth.getUsername());
    }

    // register failure (user already taken)
    @Test
    public void testRegisterUserAlreadyTaken() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        userService.register(user);
        assertThrows(DataAccessException.class, () -> userService.register(user));
    }

    // login success
    @Test
    public void testLoginSuccess() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        userService.register(user);
        AuthData auth = userService.login(user);

        assertNotNull(auth);
        assertEquals("testUser", auth.getUsername());
    }

    // login failure (not registered)
    @Test
    public void testLoginFailure() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        assertThrows(DataAccessException.class, () -> userService.login(user));
    }

    // logout success
    @Test
    public void testLogoutSuccess() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        AuthData auth = userService.register(user);
        assertDoesNotThrow(() -> userService.logout(auth.getAuthToken()));
    }

    // logout failure (invalid auth token)
    @Test
    public void testLogoutFailure() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        userService.register(user);
        assertThrows(DataAccessException.class, () -> userService.logout("auth"));
    }
}
