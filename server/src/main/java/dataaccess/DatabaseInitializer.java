package dataaccess;


public class DatabaseInitializer {
    public static void initializeDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}