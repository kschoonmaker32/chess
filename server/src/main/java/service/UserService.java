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
        if(userDAO.getUser(user.getUsername()) != null) {
            throw new DataAccessException("User not found");
        }
        userDAO.createUser(user);
        AuthData auth = new AuthData(generateAuthToken(), user.getUsername());
        authDAO.createAuth(auth);
        return auth;
    }



    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
