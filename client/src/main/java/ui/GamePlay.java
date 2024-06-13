package ui;

import java.util.Scanner;

public class GamePlay {

    private final Scanner scanner;

    public GamePlay() {
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("Enter command: Help, Redraw Chess Board, Leave, Make move, Resign, Highlight Legal Moves. ");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "help":
                    displayHelp();
                    break;
                case "redraw chess board":
                case "leave":
                case "make move":
                case "resign":
                case "highlight legal moves":
                default:
                    System.out.println("Command not recognized. Type 'Help' for a list of available commands. ");
                    break;
            }
        }
    }

    public void displayHelp() {
        System.out.println("Redraw Chess Board: redraw the current chessboard. ");
        System.out.println("Leave: leave game.");
        System.out.println("Make move: make a move when it's your turn. ");
        System.out.println("Resign: Forfeit the game. ");
        System.out.println("Highlight legal moves: highlight the possible moves for a piece of your choice. ");
    }


}
