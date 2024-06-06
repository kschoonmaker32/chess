package ui;

public class DrawBoard {

    private final char[][] board;
    private static final String whitePieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
    private static final String blackPieceColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
    private static final String whiteText = EscapeSequences.SET_TEXT_COLOR_WHITE;

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
    }
}