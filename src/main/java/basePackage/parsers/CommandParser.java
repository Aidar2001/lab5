package basePackage.parsers;

import basePackage.objectModel.Human;
import basePackage.сommander.Command;
import basePackage.сommander.Command.Argument;
import basePackage.сommander.Command.NameOfCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CommandParser {
    private ObjectMapper mapper = new ObjectMapper();

    public Command parseCommand(String rawCommand) throws IOException {
        Command command = new Command();

        String rawNameOfCommand = rawCommand;
        if (rawCommand.contains(" ")) {
            rawNameOfCommand = rawCommand.substring(0, rawCommand.indexOf(" "));
            String rawArgument = rawCommand.substring(rawCommand.indexOf(" ") + 1);

            Argument argument = command.new Argument();
            argument.setHuman(mapper.readValue(rawArgument, Human.class));
            command.setArgument(argument);
        }

        NameOfCommand nameOfCommand = NameOfCommand.valueOf(rawNameOfCommand.toUpperCase());
        command.setNameOfCommand(nameOfCommand);

        return command;

    }
}
