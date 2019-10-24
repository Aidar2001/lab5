package basePackage.commander;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class to read command from console. It use for this <code>Scanner</code>.
 */

public class CommandReader {

    private boolean firstTime = true;
    /**
     * @return string which was entered to console
     */
    public String readCommand() {
        if(firstTime) {
            System.out.println("Type 'help' to get list of commands");
            firstTime = false;
        }

        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("Enter a command:");
        while (true) {
            try {
                return consoleScanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Entered invalid command");
            }
        }
    }
}
