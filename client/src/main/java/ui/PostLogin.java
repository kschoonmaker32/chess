package ui;

import client.ServerFacade;
import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PostLogin {

    private final ServerFacade serverFacade;
    private final Scanner scanner;
    private final String authToken;
    private final Map<Integer, Integer> gameIDs;

    public PostLogin(ServerFacade serverFacade, String authToken) {
        this.serverFacade = serverFacade;
        this.authToken = authToken;
        this.scanner = new Scanner(System.in);
        this.gameIDs = new HashMap<>();
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("Enter command: Help, Logout, Create Game, List Games, Play Game, or Observe game. ");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "help":
                    displayHelp();
                    break;
                case "logout":
                    logout();
                    running = false;
                    break;
                case "create game":
                    createGame();
                    break;
                case "list games":
                    listGames();
                    break;
                case "play game" :
                    playGame();
                    break;
                case "observe game":
                    observeGame();
                    break;
                default:
                    System.out.println("Command not recognized. Type 'Help' for a list of available commands. ");
                    break;
            }
        }
    }

    private void displayHelp() {
        System.out.println("Logout: logout of account. ");
        System.out.println("Create game: create a new chess game. ");
        System.out.println("List games: show existing games. ");
        System.out.println("Play game: join an existing game. ");
        System.out.println("Observe game: watch a game being played. ");
    }

    public void logout() {
        try {
            serverFacade.logout(authToken);
            System.out.println("Logout successful");
        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }

    public void createGame() {
        System.out.println("Enter game name: ");
        String gameName = scanner.nextLine();
        try {
            serverFacade.createGame(authToken, gameName);
            System.out.println("Game created successfully!");
        } catch (Exception e) {
            System.out.println("Failed to create game: " + e.getMessage());
        }
    }

    public void listGames() {
        try {

            var games = serverFacade.listGames(authToken);
            if (games.length == 0) {
                System.out.println("No games available. ");
            } else {
                for (int i = 0; i < games.length; i ++) {
                    gameIDs.put(i+1, games[i].getGameID());
                    System.out.println((i + 1) + ". " + games[i].getGameName() + " - " + games[i].getWhiteUsername() +  " vs " + games[i].getBlackUsername());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to list games: " + e.getMessage());
        }
    }

    public void playGame() {
        System.out.println("Which game would you like to join? Please enter the game number. ");
        int gameNumber = Integer.parseInt(scanner.nextLine());
        try {
            int gameID = gameIDs.get(gameNumber);

            System.out.println("Enter color (white/black): ");
            String color = scanner.nextLine();
            serverFacade.joinGame(authToken, gameID, color);
            System.out.println("Joined game successfully!");
            DrawBoard drawBoard = new DrawBoard();
            drawBoard.drawBoard();
        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }

    public void observeGame() {
        System.out.println("Which game would you like to observe? Please enter the game number. ");
        int gameID = Integer.parseInt(scanner.nextLine());
        try {
            var games = serverFacade.listGames(authToken);
            GameData selectedGame = null;
            for (GameData game : games) {
                if(game.getGameID() == (gameID)) {
                    selectedGame = game;
                    break;
                }
            }
            if (selectedGame == null) {
                System.out.println("Game not found. ");
                return;
            }
            System.out.println("Observing game. ");
            DrawBoard drawBoard = new DrawBoard();
            drawBoard.drawBoard();
        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }
}
