package basePackage.commander;

import basePackage.objectModel.Human;
import lombok.Data;

import java.io.File;

@Data

/**This is class commands, which we read from console.*/
public class Command {
    /**
     * Name of command (<strong>add</strong> {element})
     */
    private NameOfCommand nameOfCommand;

    /**Argument command (add <strong>{element}</strong>)*/
    private Argument argument;

    /**Enum with all commands (name of commands).*/
    public enum NameOfCommand {
        INFO,
        REMOVE,
        ADD,
        ADD_IF_MAX,
        REMOVE_FIRST,
        SHOW,
        REMOVE_GREATER,
        HELP,
        IMPORT,
        EXIT
    }

    /**Class command argument. Command argument may be file if entered "import", or Human if entered another command with argument.*/
    @Data
    public class Argument {
        /**File with collection, witch we imported*/
        private File file;

        private Human human;
    }
}
