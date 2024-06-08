package ui;

import client.ServerFacade;

import java.util.Scanner;
import client.ServerFacade;

public class PreLogin {

    private final ServerFacade serverFacade;
    private final Scanner scanner;

    public PreLogin(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        this.scanner = new Scanner(System.in);
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("Enter command: Help, Quit, Login, or Register.");
            String command = scanner.nextLine().trim().toLowerCase();
            switch (command) {
                case "help":
                    displayHelp();
                    break;
                case "quit":
                    running = false;
                    break;
                case "login":
                    login();
                    break;
                case "register":
                    register();
                    break;
                default:
                    System.out.println("Command not recognized. Type 'Help' for a list of available commands. ");
                    break;
            }
        }
    }

    private void displayHelp() {
        System.out.println("Quit: exit the program. ");
        System.out.println("Login: enter username and password to log into account. ");
        System.out.println("Register: create a new account. ");
    }

    private void login() {
        System.out.println("Username: ");
        String username = scanner.nextLine();
        System.out.println("Password: ");
        String password = scanner.nextLine();
        try {
            serverFacade.login(username, password);
            System.out.println("Login successful");
            // transition to postlogin
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void register() {
        System.out.println("Create a username: ");
        String username = scanner.nextLine();
        System.out.println("Create a password: ");
        String password = scanner.nextLine();
        System.out.println("Enter an email: ");
        String email = scanner.nextLine();
        try {
            serverFacade.register(username, password, email);
            System.out.println("Registration successful");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());

        }
    }
}
