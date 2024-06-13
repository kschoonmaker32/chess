package ui;

import chess.*;
import client.ServerFacade;
import client.WebSocketFacade;

import javax.websocket.WebSocketContainer;
import java.util.Scanner;

public class GamePlay {
    private final WebSocketFacade webSocketFacade;
    private final Scanner scanner;
    private final String authToken;
    private final int gameID;
    private final ChessGame chessGame;
    private final DrawBoard drawBoard;


    public GamePlay(WebSocketFacade webSocketFacade, String authToken, int gameID, ChessGame chessGame, DrawBoard drawBoard) {
        this.webSocketFacade = webSocketFacade;
        this.scanner = new Scanner(System.in);
        this.authToken = authToken;
        this.gameID = gameID;
        this.chessGame = chessGame;
        this.drawBoard = new DrawBoard(chessGame);
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
                case "leave":
                    leaveGame();
                    running = false;
                    break;
                case "make move":
                case "resign":
                    System.out.println("Are you sure you want to resign? You will lose lol. Enter yes or no: ");
                    String answer = scanner.nextLine().trim().toLowerCase();
                    if (answer.equals("yes")) {
                        resignGame();
                        break;
                    } else {
                        break;
                    }
                case "highlight moves":
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

    public void redrawBoard() {
        boolean whiteOnBottom = chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE;
        drawBoard.redrawBoard(whiteOnBottom);
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

            ChessPiece piece =
            // check if promotion
            if (ChessMovesCalculator.isPawnPromotion())
            ChessMove move = new ChessMove(startPos, endPos,)
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
