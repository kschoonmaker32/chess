package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import chess.ChessGame;
import utils.JSONUtil;

public class GameDAOMySQL implements GameDAO{

    public GameDAOMySQL() {}

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


    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame deserializeGame(String gameState) {
        return new Gson().fromJson(gameState, ChessGame.class);

    }


}
