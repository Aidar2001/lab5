package basePackage;

import basePackage.commander.CommandHandler;
import basePackage.commander.CommandReader;
import basePackage.connect.RequestError;
import basePackage.connect.RequestResult;

import java.util.Scanner;

/**
 * @author Aidar Sinetov
 * This is main class of my project.
 */
public class CliMain {
    public static void main(String[] args) {
        Client client = new ClientImpl();

        Scanner s = new Scanner(System.in);
        String input;

        Boolean registered = null;
        String username = null;
        String password = null;
        String email = null;


        // Registration check
        while (registered == null) {
            System.out.println("Are you registered (y/n):");

            input = s.nextLine().trim();
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                registered = true;
            } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
                registered = false;
            }
        }

        System.out.println("Type you username:");
        while (username == null) {
            input = s.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            username = input;
        }

        // TODO Make a few connection tries
        try {
            RequestResult<Void> result = client.connect(username, "localhost", 6124);
            if (!result.getSuccess()) {
                System.err.println("Connection error");
                System.exit(-1);
            }
        } catch (IllegalStateException e) {
            System.err.println("Failed to establish connection with server. " + e.getMessage());
            System.exit(-1);
        }

        if (registered) {
            System.out.println("Type your password:");
            while (password == null) {
                input = s.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }
                password = input;
            }
        } else {
            System.out.println("Type your email:");
            while (email == null) {
                input = s.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }

                String wrongChars = input.replaceAll(PasswordUtils.EMAIL_REGEX, "");
                if (wrongChars.isEmpty()) {
                    email = input;
                } else {
                    System.err.println("Wrong email format. Please type again:");
                }
            }

            RequestResult<Boolean> registerResult = client.register(email);

            if (!registerResult.getSuccess()) {
                if (registerResult.getError() == RequestError.ALREADY_REGISTERED) {
                    System.err.println("User with given username already exists!");
                } else {
                    System.err.println("Some error happened");
                }
                System.exit(-1);
            } else {
                if (registerResult.getResult()) {
                    System.out.println("You were sent a email with a password. Please type it here:");
                    while (password == null) {
                        input = s.nextLine().trim();
                        if (input.trim().isEmpty()) {
                            continue;
                        }
                        password = input;
                    }
                } else {
                    System.err.println("Some error happened");
                    System.exit(-1);
                }
            }
        }

        RequestResult<Boolean> loginResult = client.login(password);
        if (!loginResult.getSuccess()) {
            System.err.println("Some error happened during authorization. Please try again");
            System.exit(-1);
        } else {
            if (loginResult.getResult()) {
                System.out.println("You've been successfully authorized!");
            } else {
                System.err.println("Authorization fail. Please try again with another username or password!");
                System.exit(-1);
            }
        }

        CommandHandler commandHandler = new CommandHandler(client);
        CommandReader commandReader = new CommandReader();
        while (true) {
            commandHandler.handle(commandReader.readCommand());
        }
    }

}
