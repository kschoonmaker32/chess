package dataaccess;

import model.UserData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOMySQL implements UserDAO {

    //figure out why it wants extends instead of implements
    @Override
    public void createUser(UserData user) throws DataAccessException {
        // establish connection with database
        try (Connection conn = DatabaseManager.getConnection()) {
            // add row to database with new user info
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getEmail());
                stmt.setString(3, user.getPassword());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            // find user with given username in database
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("password")
                        );
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting user");
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            // delete all users from database
            String sql = "DELETE FROM Users";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users from database");
        }
    }
}
