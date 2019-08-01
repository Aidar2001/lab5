package basePackage.сommander;

import java.util.Scanner;

public class CommandReader {
    public String readCommand() {
        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("Введите команду");
        String stringFromTerminal = consoleScanner.nextLine().trim();
        consoleScanner.close();
        return stringFromTerminal;
    }
}
