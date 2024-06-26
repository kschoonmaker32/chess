package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessMovesCalculator {

    public static Collection<ChessMove> moveCalculator(ChessPiece piece, ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>(); // create an empty array to add valid moves to
        int row = position.getRow()-1;
        int col = position.getColumn()-1;
        int[][] directions = getDirections(piece); // create array of arrays holding directions given piece can take
        int maxSteps = getMaxSteps(piece, position);// find out max # of steps a given piece can take

        for(int[] direction : directions) { // go through all directions (arrays) for given piece
            for(int step = 1; step <= maxSteps; step++) {
                int nextRow = row + direction[0] * step;
                int nextCol = col + direction[1] * step;

                if (nextRow < 0 || nextRow >= 8 || nextCol < 0 || nextCol >= 8) { // check if piece move is on the board
                    break;
                }

                ChessPosition nextPosition = new ChessPosition(nextRow + 1, nextCol + 1); // get next position coordinates
                ChessPiece pieceAtNextPosition = board.getPiece(nextPosition);// get piece type at next position

                if (pieceAtNextPosition == null) { // if no piece
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN && direction[1] != 0) { // if piece is a pawn and it's not moving forward
                        continue; // pawns don't move
                    }
                } else if (pieceAtNextPosition.getTeamColor() != piece.getTeamColor()) { // if there is an opponent's piece
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN && direction[1] == 0) { // pawns don't capture forward
                        continue;
                    }
                } else { // if there is a piece of the same color
                    break;
                }

                // Adding valid moves in a single section
                if (isPawnPromotion(piece, nextRow)) {
                    validMoves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(position, nextPosition, ChessPiece.PieceType.ROOK));
                } else {
                    validMoves.add(new ChessMove(position, nextPosition, null));
                    if (pieceAtNextPosition != null && pieceAtNextPosition.getTeamColor() != piece.getTeamColor()) {
                        break; // break if it was a capture
                    }
                }
            }
        }
        return validMoves;
    }

    private static int[][] getDirections(ChessPiece piece) { // create object of chessPiece called piece and use it to get color and type
        ChessPiece.PieceType pieceType = piece.getPieceType();
        ChessGame.TeamColor pieceColor = piece.getTeamColor();

        switch (pieceType) {
            case BISHOP:
                return new int[][]{{1, 1}, {-1, 1}, {-1, -1}, {1, -1}}; // bishop can only move in diagonals
            case ROOK:
                return new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
            case QUEEN:
                return new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
            case KNIGHT:
                return new int[][]{{-1, 2}, {1, 2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}};
            case KING:
                return new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            case PAWN:
                if(pieceColor == ChessGame.TeamColor.WHITE) {
                    return new int[][]{{1,0}, {1,1}, {1,-1}};
                } else {
                    return new int[][]{{-1,0}, {-1,-1}, {-1,1}};
                }
            default:
                return new int[][]{};
        }
    }

    private static int getMaxSteps(ChessPiece piece, ChessPosition position) {
        return switch (piece.getPieceType()) {
            case KNIGHT, KING -> 1;
            case PAWN -> piece.pawnFirstMove(position) ? 2 : 1;
            default -> 7;
        };
    }

    public static boolean isPawnPromotion(ChessPiece piece, int nextRow) {
        if(piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            return (piece.getTeamColor() == ChessGame.TeamColor.WHITE && nextRow == 7) ||
                    (piece.getTeamColor() == ChessGame.TeamColor.BLACK && nextRow == 0);
        }
        return false;
    }
}
