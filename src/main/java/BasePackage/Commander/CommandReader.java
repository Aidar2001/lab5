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
        System.out.println("Введите команду");
        String command = consoleScanner.nextLine().trim();
        return command.split(" ", 2);
    }
}
