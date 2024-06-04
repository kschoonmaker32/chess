package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.UserData;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOMySQLTest {

    private UserDAOMySQL userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        DatabaseInitializer.initializeDatabase();
        userDAO = new UserDAOMySQL();
        userDAO.clear();
    }

    // create user success
    @Test
    public void testCreateUserSuccess() throws DataAccessException {
        UserData user = new UserData("username", "email@example.com", "password");
        userDAO.createUser(user);

        UserData retrievedUser = userDAO.getUser("username");
        assertNotNull(retrievedUser);
        assertEquals("username", retrievedUser.getUsername());
    }

    // create user failure (create user twice)
    @Test
    public void testCreateUserFailure() throws DataAccessException { // never thrown
        UserData user1 = new UserData("username", "email@example.com", "password");
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(user1);
            userDAO.createUser(user1);
        });
    }

    // get user success
    @Test
    public void testGetUserSuccess() throws DataAccessException {
        UserData user = new UserData("username", "password", "email@example.com");
        userDAO.createUser(user);

        UserData retrievedUser = userDAO.getUser("username");
        assertNotNull(retrievedUser);
        assertEquals("username", retrievedUser.getUsername());
        assertEquals("password", retrievedUser.getPassword());
        assertEquals("email@example.com", retrievedUser.getEmail());
    }

    // get user failure (user not found)
    @Test
    public void testGetUserFailure() throws DataAccessException {
        assertNull(userDAO.getUser("username1"));
    }

    // clear users
    @Test
    public void testClearUsersSuccess() throws DataAccessException {
        UserData user1 = new UserData("username1", "email1@example.com", "password1");
        UserData user2 = new UserData("username2", "email2@example.com", "password2");
        userDAO.createUser(user1);
        userDAO.createUser(user2);

        userDAO.clear();
        assertNull(userDAO.getUser("username1"));
        assertNull(userDAO.getUser("username2"));
    }
}
