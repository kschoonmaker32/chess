package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTeam;
    private ChessBoard board;

    public ChessGame() {
        this.currentTeam = TeamColor.WHITE;// since white starts
        //this.board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeam = team;
    }

    public void switchTeams() {
        if(currentTeam == TeamColor.WHITE) {
            currentTeam = TeamColor.BLACK;
        } else {
            currentTeam = TeamColor.WHITE;
        }
    }
    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        if(piece == null) return null;
        return piece.pieceMoves(getBoard(), startPosition);
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
       ChessPosition start = move.getStartPosition();
       ChessPosition end = move.getEndPosition();
       ChessPiece piece = getBoard().getPiece(start);

       if(piece == null){
           throw new InvalidMoveException("No piece at starting position,");

       } if(piece.getTeamColor() != currentTeam) {
           throw new InvalidMoveException("Not this team's turn.");
        }

       if(!validMoves(start).contains(move)) {
           throw new InvalidMoveException("This move is not allowed.");
       }

       ChessBoard clonedBoard = getBoard().cloneBoard();

       clonedBoard.addPiece(end, piece);
       clonedBoard.addPiece(start, null);



    }

    private void executeMove(ChessBoard board, ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);

        if(piece == null) {
            throw new InvalidMoveException("Need piece to move.");
            }

        ChessPiece pieceAtEnd = board.getPiece(end);
        if(pieceAtEnd == null) {
            board.addPiece(end, piece); // put piece in new position
            board.removePiece(start); // delete piece from old position
        } else {
            if(pieceAtEnd.getTeamColor() != piece.getTeamColor()) {
                board.removePiece(end); // capture opponent piece
                board.addPiece(end, piece); // put piece in new position
                board.removePiece(start); // delete piece from old position
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard board = getBoard();
        ChessPosition kingPosition = board.findKing(teamColor);
        int numRows = board.getRowCount();
        int numCols = board.getColCount(numRows);

        ChessGame.TeamColor opposingColor = (teamColor == TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

        for(int i =0; i < numRows; i++) {
            for(int j=0; j < numCols; j++) {
                ChessPosition piecePosition = new ChessPosition(i+1, j+1);
                ChessPiece piece = board.getPiece(piecePosition);
                if(piece != null && piece.getTeamColor() == opposingColor) {
                    Collection<ChessMove> validMoves = ChessMovesCalculator.moveCalculator(piece, board, piecePosition);
                    for(ChessMove move : validMoves) {
                        if(move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            return false;
        }
        ChessBoard board = getBoard();

        for(int i = 0; i < board.getRowCount(); i++) {
            for(int j = 0; j < board.getColCount(board.getRowCount()); j++) {
                ChessPosition piecePosition = new ChessPosition(i+1, j+1);
                ChessPiece piece = board.getPiece(piecePosition);
                if(piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = ChessMovesCalculator.moveCalculator(piece, board, piecePosition);
                    for(ChessMove move : validMoves) {
                        ChessBoard clonedBoard = board.cloneBoard();
                    }
                }
            }
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        if(board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
