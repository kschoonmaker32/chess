package service;

import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import java.util.UUID;


public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (userDAO.getUser(user.getUsername()) != null) {
            throw new DataAccessException("User already exists.");
        }
        userDAO.createUser(user);
        AuthData auth = new AuthData(generateAuthToken(), user.getUsername());
        authDAO.createAuth(auth);
        return auth;
    }

    public AuthData login(UserData user) throws DataAccessException {
        //retrieve user from database here
        UserData existingUser = userDAO.getUser(user.getUsername());
        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            throw new DataAccessException("Invalid credentials.");
        }
        AuthData auth = new AuthData(generateAuthToken(), user.getUsername());
        authDAO.createAuth(auth);
        return auth;
    }

    public void logout(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private String getUsername(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken).getUsername();
    }
}
