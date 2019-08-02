package basePackage.objectModel;

import basePackage.exeptions.NotCorrectNameExeption;
import basePackage.exeptions.NotFoundNameException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Creature{
    @XmlAttribute
    protected String name;

    public Creature(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        if(newName==null){
            try {
                throw new NotFoundNameException();
            } catch (NotFoundNameException e) {
                e.printStackTrace();
            }
        }
        else{ if (newName.length() > 100 || newName.length() == 0)
            try {
                throw new NotCorrectNameExeption(newName,"имена существ должны содержать более 0, но менее 100 символов. " );
            } catch (NotCorrectNameExeption ex) {
                ex.printStackTrace();
            }
        }
        name = newName;
    }

}
