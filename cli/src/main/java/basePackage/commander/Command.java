package basePackage.commander;

import basePackage.NameOfCommand;
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


    /**Class command argument. Command argument may be file if entered "import", or Human if entered another command with argument.*/
    @Data
    public class Argument {
        /**File with collection, witch we imported*/
        private File file;
        private int humanId;
        private Human human;
    }
}
