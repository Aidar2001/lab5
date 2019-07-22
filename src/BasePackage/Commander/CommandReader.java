package BasePackage.Commander;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class CommandReader {
    String stringFromTerminal;

    public CommandReader(String stringFromTerminal) {
        this.stringFromTerminal = stringFromTerminal;
    }
    private String[] readCommand() {
        Scanner consoleScanner = new Scanner(System.in);
        try {
            String command;
            System.out.println("Введите команду");
            command = consoleScanner.nextLine();
            command = command.trim();
            String[] fullCommand = command.split(" ", 2);
            return fullCommand;
        } catch (NoSuchElementException ex) {
            consoleScanner.close();
        }
        return null;
    }
}
