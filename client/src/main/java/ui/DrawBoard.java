package ui;

public class DrawBoard {

    private final char[][] board;
    private static final String WHITE_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_RED;
    private static final String BLACK_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_BLACK;
    private static final String WHITE_TEXT = EscapeSequences.SET_TEXT_COLOR_WHITE;

    public DrawBoard() {
        board = new char[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        // i = rows   j = cols

        // add white pieces (red on board)
        board[0][0] = 'R';
        board[0][1] = 'N';
        board[0][2] = 'B';
        board[0][3] = 'K';
        board[0][4] = 'Q';
        board[0][5] = 'B';
        board[0][6] = 'N';
        board[0][7] = 'R';
        board[1][0] = 'P';
        board[1][1] = 'P';
        board[1][2] = 'P';
        board[1][3] = 'P';
        board[1][4] = 'P';
        board[1][5] = 'P';
        board[1][6] = 'P';
        board[1][7] = 'P';

        // add black pieces (blue on board)
        board[7][0] = 'r';
        board[7][1] = 'n';
        board[7][2] = 'b';
        board[7][3] = 'k';
        board[7][4] = 'q';
        board[7][5] = 'b';
        board[7][6] = 'n';
        board[7][7] = 'r';
        board[6][0] = 'p';
        board[6][1] = 'p';
        board[6][2] = 'p';
        board[6][3] = 'p';
        board[6][4] = 'p';
        board[6][5] = 'p';
        board[6][6] = 'p';
        board[6][7] = 'p';
        // initialize empty spaces
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public void drawBoard() {
        drawBoardState(false);
        drawBoardState(true);
    }

    private void drawBoardState(boolean whiteOnBottom) {
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
                char piece = board[row][col];
                char capitalPiece = Character.toUpperCase(piece);
                String pieceColor = getPieceColor(piece);
                if ((row + col) % 2 == 0) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_BLUE + " " + pieceColor + capitalPiece + WHITE_TEXT + " " + EscapeSequences.RESET_BG_COLOR);
                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + " " + pieceColor + capitalPiece + WHITE_TEXT + " " + EscapeSequences.RESET_BG_COLOR);
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

    private String getPieceColor(char piece) {
        if (Character.isUpperCase(piece)) {
            return WHITE_PIECE_COLOR;
        } else {
            return BLACK_PIECE_COLOR;
        }
    }
}