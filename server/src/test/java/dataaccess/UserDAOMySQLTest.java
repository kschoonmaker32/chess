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
    public void testCreateUserFailure() throws DataAccessException {
        UserData user = new UserData("username", "email@example.com", "password");
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(user);
            userDAO.createUser(user);
        });
    }

    // get user success
    @Test
    public void testGetUserSuccess() throws DataAccessException {
        UserData user = new UserData("username", "email@example.com", "password");
        userDAO.createUser(user);

        UserData retrievedUser = userDAO.getUser("username");
        assertNotNull(retrievedUser);
        assertEquals("username", retrievedUser.getUsername());
        assertEquals("email@example.com", retrievedUser.getEmail());
        assertEquals("password", retrievedUser.getPassword());
    }

    // get user failure (user not found)
    @Test
    public void testGetUserFailure() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("username");
        });
    }

    // clear users
    @Test
    public void testClearUsersSuccess() throws DataAccessException {
        UserData user1 = new UserData("username1", "email1@example.com", "password1");
        UserData user2 = new UserData("username2", "email2@example.com", "password2");
        userDAO.createUser(user1);
        userDAO.createUser(user2);

        userDAO.clear();
        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("username1");
        });
        assertThrows(DataAccessException.class, () -> {
            userDAO.getUser("username2");
        });
    }
}
