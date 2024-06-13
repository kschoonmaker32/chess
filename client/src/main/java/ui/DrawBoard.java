package ui;

import chess.*;

public class DrawBoard {

    private ChessGame chessGame;
    private static final String WHITE_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_RED;
    private static final String BLACK_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_BLACK;
    private static final String WHITE_TEXT = EscapeSequences.SET_TEXT_COLOR_WHITE;

    public DrawBoard(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    private void drawBoard(boolean whiteOnBottom) {
        if (whiteOnBottom) {
            System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + WHITE_TEXT + "    a  b  c  d  e  f  g  h    " + EscapeSequences.RESET_BG_COLOR);
        } else {
            System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + WHITE_TEXT + "    h  g  f  e  d  c  b  a    " + EscapeSequences.RESET_BG_COLOR);
        }

        for (int i = 0; i < 8; i++) {
            int row = whiteOnBottom ? 7 - i : i;
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + (row + 1) + " " + EscapeSequences.RESET_BG_COLOR);
            for (int j = 0; j < 8; j++) {
                int col = whiteOnBottom ? j : 7 - j;
                ChessPiece piece = chessGame.getBoard().getPiece(new ChessPosition(row + 1, col + 1));
                char pieceChar = getPieceType(piece);
                String pieceColor = piece != null && piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR;
                if ((row + col) % 2 == 0) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BLUE + " " + pieceColor + pieceChar + WHITE_TEXT + " " + EscapeSequences.RESET_BG_COLOR);
                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + " " + pieceColor + pieceChar + WHITE_TEXT + " " + EscapeSequences.RESET_BG_COLOR);
                }
            }
            System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + (row + 1) + " " + EscapeSequences.RESET_BG_COLOR);
        }
        if (whiteOnBottom) {
            System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + WHITE_TEXT + "    a  b  c  d  e  f  g  h    " + EscapeSequences.RESET_BG_COLOR);
        } else {
            System.out.println(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + WHITE_TEXT + "    h  g  f  e  d  c  b  a    " + EscapeSequences.RESET_BG_COLOR);
        }
    }

    private char getPieceType(ChessPiece piece) {
        char type = 0;
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            type = 'P';
        } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            type = 'B';
        } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
            type = 'Q';
        } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            type = 'K';
        } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            type = 'R';
        } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            type = 'N';
        } return type;
    }

    public int[] convertPositionToIndices(String position) {
        if (position.length() != 2) {
            return null;
        }
        char column = position.charAt(0);
        char row = position.charAt(1);

        int colIndex = column - 'a';
        int rowIndex = row - 1;
        if (colIndex < 0 || colIndex > 7 || rowIndex < 0 || rowIndex > 7) {
            return null;
        }
        return new int[]{rowIndex, colIndex};
    }

    public void redrawBoard(boolean whiteOnBottom) {
        drawBoard(whiteOnBottom);
    }

}