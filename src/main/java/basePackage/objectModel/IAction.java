package basePackage.objectModel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = Action.class)
public interface IAction {
    String getActionName(); // lab6
}
