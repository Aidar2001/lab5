package basePackage.commander;

import basePackage.NameOfCommand;
import basePackage.commander.Command.*;
import basePackage.objectModel.Human;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * This class parse command from console and use for this <code>ObjectMapper</code>.
 */
public class CommandParser {
    private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    /**
     * @param rawCommand
     * @return object of {@link Command}
     * @throws IOException if method can not deserialize argument of entered command
     */
    public Command parseCommand(String rawCommand) throws IOException {
        Command command = new Command();

        String rawNameOfCommand = rawCommand;
        if (rawCommand.contains(" ")) {
            rawNameOfCommand = rawCommand.substring(0, rawCommand.indexOf(" "));
            String rawArgument = rawCommand.substring(rawCommand.indexOf(" ") + 1);

            Argument argument = command.new Argument();
            if (rawNameOfCommand.equals("import")) {
                File file = getFileByPath(rawArgument);
                argument.setFile(file);
            } else {
                if (isDigit(rawArgument)) {
                    argument.setHumanId(Integer.parseInt(rawArgument));
                } else {
                    argument.setHuman(mapper.readValue(rawArgument, Human.class));
                }

            }
            command.setArgument(argument);
        }


        NameOfCommand nameOfCommand = NameOfCommand.valueOf(rawNameOfCommand.toUpperCase());
        command.setNameOfCommand(nameOfCommand);

        return command;

    }

    private File getFileByPath(String rawArgument) {
        return new File(rawArgument);

    }

    private boolean isDigit(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
