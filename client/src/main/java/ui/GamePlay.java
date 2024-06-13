package ui;

import client.ServerFacade;
import client.WebSocketFacade;

import javax.websocket.WebSocketContainer;
import java.util.Scanner;

public class GamePlay {
    private final ServerFacade serverFacade;
    private final WebSocketFacade webSocketFacade;
    private final Scanner scanner;
    private final String authToken;
    private final int gameID;


    public GamePlay(ServerFacade serverFacade, WebSocketFacade webSocketFacade, String authToken, int gameID) {
        this.serverFacade = serverFacade;
        this.webSocketFacade = webSocketFacade;
        this.scanner = new Scanner(System.in);
        this.authToken = authToken;
        this.gameID = gameID;
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

    public void leaveGame() {
        try {
            webSocketFacade.sendLeave(authToken, gameID);
            System.out.println("Left the game. ");
        } catch (Exception e) {
            System.out.println("Failed to leave the game: " + e.getMessage());
        }
    }

}
