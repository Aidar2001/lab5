package BasePackage.ObjectModel;

import BasePackage.Exeptions.NotCorrectNameExeption;
import BasePackage.Exeptions.NotFoundNameException;

public class Action implements IAction {
    private String actionName;
    private int id;
    private static int count = 0;


    public Action(String NameAction) {
        try {
            setActionName(NameAction);
            id = ++count;
        } catch (NotCorrectNameExeption e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getActionName() {
        return actionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return actionName == action.actionName;
    }

    public void setActionName(String actionName) throws NotCorrectNameExeption {
        try {
            if (actionName == null) {
                throw new NotFoundNameException();
            } else {
                if (actionName.matches("[a-zA-ZА-Яа-я]") || actionName.length() == 0) {
                    throw new NotCorrectNameExeption();
                }
            }
        } catch (NotFoundNameException e) {
            e.printStackTrace();
        }
        this.actionName = actionName;
    }
    @Override
    public String toString() {
        return getActionName();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        return PRIME * id;
    }
}
