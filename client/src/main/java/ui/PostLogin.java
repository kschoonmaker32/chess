package ui;

import chess.ChessGame;
import client.ServerFacade;
import client.WebSocketFacade;
import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PostLogin {

    private final ServerFacade serverFacade;
    private final WebSocketFacade webSocketFacade;
    private final Scanner scanner;
    private final String authToken;
    private final Map<Integer, Integer> gameIDs;

    public PostLogin(ServerFacade serverFacade, WebSocketFacade webSocketFacade, String authToken) {
        this.serverFacade = serverFacade;
        this.webSocketFacade = webSocketFacade;
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

            ChessGame chessGame = new ChessGame();
            DrawBoard drawBoard = new DrawBoard(chessGame);
            ChessGame.TeamColor userColor = color.equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            GamePlay gamePlay = new GamePlay(webSocketFacade, authToken, gameID, chessGame, drawBoard);
            gamePlay.display();
        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }

    public void observeGame() {
        System.out.println("Which game would you like to observe? Please enter the game number. ");
        int gameNumber = Integer.parseInt(scanner.nextLine());
        try {
            int gameID = gameIDs.get(gameNumber);

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

            // set up board
            ChessGame chessGame = new ChessGame();
            chessGame.setBoard(selectedGame.getGame().getBoard());
            DrawBoard drawBoard = new DrawBoard(chessGame);
            // start gameplay ui
            GamePlay gamePlay = new GamePlay(webSocketFacade, authToken, gameID, chessGame, drawBoard);
            gamePlay.display();

        } catch (Exception e) {
            System.out.println("Failed to join game: " + e.getMessage());
        }
    }
}
