package ui;

import chess.*;
import client.ServerFacade;
import client.WebSocketFacade;

import javax.websocket.WebSocketContainer;
import java.util.Collection;
import java.util.Scanner;

public class GamePlay {
    private final WebSocketFacade webSocketFacade;
    private final Scanner scanner;
    private final String authToken;
    private final int gameID;
    private final ChessGame chessGame;
    private final DrawBoard drawBoard;
    private final ChessGame.TeamColor userColor;


    public GamePlay(WebSocketFacade webSocketFacade, String authToken, int gameID, ChessGame chessGame, DrawBoard drawBoard, ChessGame.TeamColor userColor) {
        this.webSocketFacade = webSocketFacade;
        this.scanner = new Scanner(System.in);
        this.authToken = authToken;
        this.gameID = gameID;
        this.chessGame = chessGame;
        this.drawBoard = new DrawBoard(chessGame);
        this.userColor = userColor;

    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("Enter command: Help, Redraw, Leave, Make move, Resign, Highlight Moves. ");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "help":
                    displayHelp();
                    break;
                case "redraw":
                    drawBoard.redrawBoard(userColor == ChessGame.TeamColor.WHITE);
                    break;
                case "leave":
                    leaveGame();
                    running = false;
                    break;
                case "make move":
                    makeMove();
                    break;
                case "resign":
                    System.out.println("Are you sure you want to resign? Enter yes or no: ");
                    String answer = scanner.nextLine().trim().toLowerCase();
                    if (answer.equals("yes")) {
                        resignGame();
                    }
                    break;
                case "highlight moves":
                    highlightMoves();
                    break;
                default:
                    System.out.println("Command not recognized. Type 'Help' for a list of available commands. ");
                    break;
            }
        }
    }

    public void displayHelp() {
        System.out.println("Redraw: redraw the current chessboard. ");
        System.out.println("Leave: leave game.");
        System.out.println("Make move: make a move when it's your turn. ");
        System.out.println("Resign: Forfeit the game. ");
        System.out.println("Highlight moves: highlight the possible moves for a piece of your choice. ");
    }

    public void leaveGame() {
        try {
            webSocketFacade.sendLeave(authToken, gameID);
            System.out.println("Left the game. ");
        } catch (Exception e) {
            System.out.println("Failed to leave the game: " + e.getMessage());
        }
    }

    public void resignGame() {
        try {
            webSocketFacade.sendResign(authToken, gameID);
            System.out.println("You have resigned from the game. ");
        } catch (Exception e) {
            System.out.println("Failed to leave the game: " + e.getMessage());
        }
    }

    public void makeMove() {
        try {
            System.out.println("Which piece would you like to move? Enter the letter and the number (e.g. e2): ");
            String startPos = scanner.nextLine();
            System.out.println("Where would you like to move this piece? Enter the letter and the number: ");
            String endPos = scanner.nextLine();

            ChessPosition start = convertPositionToIndices(startPos);
            ChessPosition end = convertPositionToIndices(endPos);
            if (start == null || end == null) {
                System.out.println("Invalid position entered. ");
                return;
            }
            ChessPiece piece = chessGame.getBoard().getPiece(start);

            Collection<ChessMove> validMoves = chessGame.validMoves(start);

            ChessMove move;
            // if pawn promotion:
            if (piece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN && ChessMovesCalculator.isPawnPromotion(piece, end.getRow())) {
                ChessPiece.PieceType promoPiece = null;

                while (promoPiece == null) {
                    System.out.println("Pawn promotion! Choose a piece to promote to: (Queen, Rook, Bishop, Knight) ");
                    String promoString = scanner.nextLine().trim().toLowerCase();
                    switch (promoString) {
                        case "queen" -> promoPiece = ChessPiece.PieceType.QUEEN;
                        case "rook" -> promoPiece = ChessPiece.PieceType.ROOK;
                        case "bishop" -> promoPiece = ChessPiece.PieceType.BISHOP;
                        case "knight" -> promoPiece = ChessPiece.PieceType.KNIGHT;
                        default -> System.out.println("Invalid promotion piece. Please choose again. ");
                    }
                }
                move = new ChessMove(start, end, promoPiece);
            // if not pawn promotion:
            } else {
                move = new ChessMove(start, end, null);
            }
            if (!validMoves.contains(move)) {
                System.out.println("This move is not allowed.");
            }

            webSocketFacade.sendMakeMove(authToken, gameID, move);
            chessGame.makeMove(move); // execute move
            System.out.println("Moved successfully. ");
            drawBoard.redrawBoard(userColor == ChessGame.TeamColor.WHITE);
        } catch (Exception e) {
            System.out.println("Failed to make move: " + e.getMessage());
        }
    }

    public void highlightMoves() {
        try {
            System.out.println("Enter the position of the piece you would like to highlight moves for: (e.g. e2) ");
            String startPos = scanner.nextLine();

            ChessPosition start = convertPositionToIndices(startPos);
            if (start == null) {
                System.out.println("Invalid position entered. ");
                return;
            }
            Collection<ChessMove> moves = chessGame.validMoves(start);
            boolean whiteOnBottom = chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE;

            drawBoard.highlightMoves(whiteOnBottom, moves);
        } catch (Exception e) {
            System.out.println("Failed to highlight moves: " + e.getMessage());
        }

    }

    private ChessPosition convertPositionToIndices(String position) {
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
        return new ChessPosition(rowIndex, colIndex);
    }
}
