package basePackage.parsers;

import basePackage.commander.Command;
import basePackage.commander.Command.Argument;
import basePackage.commander.Command.NameOfCommand;
import basePackage.objectModel.Human;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CommandParser {
    private ObjectMapper mapper = new ObjectMapper();

    {

    }

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
                argument.setHuman(mapper.readValue(rawArgument, Human.class));
            }
            command.setArgument(argument);
        }

        NameOfCommand nameOfCommand = NameOfCommand.valueOf(rawNameOfCommand.toUpperCase());
        command.setNameOfCommand(nameOfCommand);

        return command;

    }

    private File getFileByPath(String rawArgument) {
        return new File(rawArgument);//TODO обработать исключения - абсолютный/относительный путь, dev/null, dev/zero, dev/random

    }
}
