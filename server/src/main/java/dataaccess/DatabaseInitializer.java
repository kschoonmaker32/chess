package dataaccess;

import dataaccess.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initializeDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();

    }

    private static void createTables() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            // create users table here
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(255) PRIMARY KEY," +
                    "email VARCHAR(255) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL";
            try (PreparedStatement stmt = conn.prepareStatement(createUsersTable)) {
                stmt.executeUpdate();
            }

            // create games table here
            String createGamesTable = "CREATE TABLE IF NOT EXISTS games (" +

        } catch (SQLException e) {
            throw new DataAccessException("Error creating tables: " + e.getMessage());
        }
    }
}