package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.List;

public class GameDAOMySQL implements GameDAO{

    //public GameDAOMySQL() {}

    @Override
    public void createGame(GameData game) throws DataAccessException {
        String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, gameState) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, game.getGameID());
            stmt.setString(2, game.getWhiteUsername());
            stmt.setString(3, game.getBlackUsername());
            stmt.setString(4, game.getGameName());
            stmt.setString(5, serializeGame(game.getGame()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game in database");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM games where WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                 stmt.setInt(1, gameID);
                 ResultSet rs = stmt.executeQuery();
                 if (rs.next()) {
                     return new GameData(
                        rs.getInt("gameID"),
                        rs.getString("whiteUsername"),
                        rs.getString("blackUsername"),
                        rs.getString("gameName"),
                        deserializeGame(rs.getString("gameState"))
                     );
                 } else {
                     throw new DataAccessException("Game not found");
                 }
             } catch (SQLException e) {
                 throw new DataAccessException("Error querying game from database");
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException { // figure out exception here
        String sql = "SELECT * FROM games";
        List<GameData> games = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt  = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                games.add(new GameData(
                    rs.getInt("gameID"),
                    rs.getString("whiteUsername"),
                    rs.getString("blackUsername"),
                    rs.getString("gameName"),
                    deserializeGame(rs.getString("gameState"))
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing games from database");
        }
        return games;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameState = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt  = conn.prepareStatement(sql)) {
            stmt.setString(1, game.getWhiteUsername());
            stmt.setString(1, game.getBlackUsername());
            stmt.setString(1, game.getGameName());
            stmt.setString(1, serializeGame(game.getGame()));
            stmt.setInt(1, game.getGameID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game in database");
        }

        // find out why sometimes result and sometimes not
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing games from database");
        }
    }


    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame deserializeGame(String gameState) {
        return new Gson().fromJson(gameState, ChessGame.class);

    }


}
