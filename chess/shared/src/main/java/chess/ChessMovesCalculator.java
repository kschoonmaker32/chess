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

                if(nextRow < 0 || nextRow >= 8 || nextCol < 0 || nextCol >= 8) { // check if piece move is on the board
                    break;
                }

                ChessPosition nextPosition = new ChessPosition(nextRow+1, nextCol+1); // get next position coordinates
                ChessPiece pieceAtNextPosition = board.getPiece(nextPosition);// get piece type at next position


                if(pieceAtNextPosition == null) {// if no piece
                    if(piece.getPieceType() == ChessPiece.PieceType.PAWN && direction[1] != 0) { // if piece is a pawn and its not moving forward
                        continue; //pawns don't move
                    }
                    validMoves.add(new ChessMove(position, nextPosition, null));
                } else if(pieceAtNextPosition.getTeamColor() != piece.getTeamColor()) {
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN && direction[1] == 0) {
                        continue;
                    }
                    validMoves.add(new ChessMove(position, nextPosition, null));
                } else {
                        break;
                    }
                }

//                if(pieceAtNextPosition == null) { // if no piece, add movement
//                    validMoves.add(new ChessMove(position, nextPosition, null));
//                } else if(pieceAtNextPosition.getTeamColor() != piece.getTeamColor()) { // if theres an opponent piece, add movement
//                    validMoves.add(new ChessMove(position, nextPosition, null));
//                    break; // stop moving after piece is captured
//                } else {
//                    break; //otherwise break out of loop because piece cannot move there
//                }
        }
        return validMoves;
    }

    public static int[][] getDirections(ChessPiece piece) { // create object of chessPiece called piece and use it to get color and type
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
//                if(pieceColor == ChessGame.TeamColor.WHITE){
//                    return new int[][]{{0,2}, {0,1}, {1, -1}, {1,1}};
//                } else {
//                    return new int[][]{{0,-2}, {0,-1}, {-1,-1}, {-1,1}};
//                }
            default:
                return new int[][]{};
        }
    }

    public static int getMaxSteps(ChessPiece piece, ChessPosition position) {
        return switch (piece.getPieceType()) {
            case KNIGHT, KING -> 1;
            case PAWN -> piece.pawnFirstMove(position) ? 2 : 1;
            default -> 7;
        };
    }

    private static boolean isPawnPromotion(ChessPiece piece, int nextRow) {
        return (piece.getTeamColor() == ChessGame.TeamColor.WHITE && nextRow == 7) ||
                (piece.getTeamColor() == ChessGame.TeamColor.BLACK && nextRow == 0);
    }
}
