package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.UserData;
import model.AuthData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    @Test
    public void testRegisterSuccess() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        AuthData auth = userService.register(user);

        assertNotNull(auth);
        assertEquals("testUser", auth.getUsername());
    }

    @Test
    public void testRegisterUserAlreadyTaken() throws Exception {
        UserData user = new UserData("testUser", "password123", "test@example.com");
        userService.register(user);
        assertThrows(DataAccessException.class, () -> userService.register(user));
    }
}
