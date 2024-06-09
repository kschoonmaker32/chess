package ui;

import client.ServerFacade;
import model.GameData;

import java.util.Scanner;

public class PostLogin {

        private final ServerFacade serverFacade;
        private final Scanner scanner;
        private final String authToken;

        public PostLogin(ServerFacade serverFacade, String authToken) {
            this.serverFacade = serverFacade;
            this.authToken = authToken;
            this.scanner = new Scanner(System.in);
        }

        public void display() {
            boolean running = true;
            while (running) {
                System.out.println("Enter command: Help, Logout, Create Game, List Games,  or Play Game");
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
                        // implement this in phase 6
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
                for (int i = 0; i <= games.length; i ++) {
                    System.out.println((i + 1) + ". " + games[i].getGameName() + " - " + games[i].getWhiteUsername() +  " vs " + games[i].getBlackUsername());
                }
            } catch (Exception e) {
                System.out.println("Failed to list games: " + e.getMessage());
            }
        }

        public void playGame() {
            System.out.println("Which game would you like to join? ");
            String gameName = scanner.nextLine();
            try {
                var games = serverFacade.listGames(authToken);
                GameData selectedGame = null;
                for (GameData game : games) {
                    if(game.getGameName().equalsIgnoreCase(gameName)) {
                        selectedGame = game;
                        break;
                    }
                }
                if (selectedGame == null) {
                    System.out.println("Game not found. ");
                    return;
                }

                int gameID = selectedGame.getGameID();
                System.out.println("Enter color (white/black): ");
                String color = scanner.nextLine();
                serverFacade.joinGame(authToken, gameID, color);
                System.out.println("Joined game successfully!");
            } catch (Exception e) {
                System.out.println("Failed to join game: " + e.getMessage());
            }
        }
}
