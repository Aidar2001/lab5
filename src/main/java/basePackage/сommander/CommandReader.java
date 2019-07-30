package basePackage.сommander;

import java.util.Scanner;

public class CommandReader {
    String stringFromTerminal;

    public CommandReader(String stringFromTerminal) {
        this.stringFromTerminal = stringFromTerminal;
    }

    private String readCommand() {
        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("Введите команду");
        return consoleScanner.nextLine().trim();

    }
}
