package ui;

import client.ServerFacade;

import java.util.Locale;
import java.util.Scanner;

public class PreLogin {

    //private final ServerFacade serverFacade;
    private final Scanner scanner;

    public Prelogin(ServerFacade serverFacade) {
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


}
