package ui;

import chess.*;

import java.util.Collection;

public class DrawBoard {

    private ChessGame chessGame;
    private static final String WHITE_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_RED;
    private static final String BLACK_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_BLACK;
    private static final String WHITE_TEXT = EscapeSequences.SET_TEXT_COLOR_WHITE;
    private static final String HIGHLIGHT_COLOR = EscapeSequences.SET_BG_COLOR_YELLOW;

    public DrawBoard(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public void drawChessBoard(boolean whiteOnBottom) {
        drawBoardState(whiteOnBottom, null);
    }

    public void highlightMoves(boolean whiteOnBottom, Collection<ChessMove> moves) {
        drawBoardState(whiteOnBottom, moves);
    }

    private void drawBoardState(boolean whiteOnBottom, Collection<ChessMove> moves) {
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
                ChessPosition pos = new ChessPosition(row + 1, col + 1);
                boolean isHighlight = moves != null && moves.stream().anyMatch(move -> move.getEndPosition().equals(pos));

                String bgColor = (row + col) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_LIGHT_BLUE : EscapeSequences.SET_BG_COLOR_WHITE;
                if (isHighlight) {
                    System.out.print(HIGHLIGHT_COLOR + " " + pieceColor + pieceChar + WHITE_TEXT + " " + EscapeSequences.RESET_BG_COLOR);
                } else {
                    System.out.print(bgColor + " " + pieceColor + pieceChar + WHITE_TEXT + " " + EscapeSequences.RESET_BG_COLOR);
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
        if (piece == null) return ' ';
        return switch (piece.getPieceType()) {
            case PAWN -> 'P';
            case BISHOP -> 'B';
            case QUEEN -> 'Q';
            case KING -> 'K';
            case ROOK -> 'R';
            case KNIGHT -> 'N';
        };
    }

    public void redrawBoard(boolean whiteOnBottom) {
        drawChessBoard(whiteOnBottom);
    }
}