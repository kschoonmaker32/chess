package service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.UserService;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import model.UserData;
import model.AuthData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserServiceTest {
    private UserService userService;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    @BeforeAll
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
    } // go over what this means
}
