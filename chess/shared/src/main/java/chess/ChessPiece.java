package chess;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    //assigning each chess piece a color and type that is private and unmutable
    private final ChessGame.TeamColor  pieceColor;
    private final ChessPiece.PieceType pieceType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    // setting current object as instance variables for color and type
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    // prints out in readable fashion
    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + pieceType +
                '}';
    }

    // checks if a piece has the same type and color as another piece
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o; // casting line
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }
    // assigns a "locker number" to each piece for aid in finding where a piece is stored
    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    public boolean pawnFirstMove(ChessPosition position) {
        if(this.pieceType != PieceType.PAWN) { //only call function on pawns
            return false;
        } else {
            return (this.pieceColor == ChessGame.TeamColor.WHITE && position.getRow() == 2) ||
                    (this.pieceColor == ChessGame.TeamColor.BLACK && position.getRow() == 7);
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board == null || myPosition == null) {
            return Collections.emptyList();
        }
        return ChessMovesCalculator.moveCalculator(this, board, myPosition);

    }
}
