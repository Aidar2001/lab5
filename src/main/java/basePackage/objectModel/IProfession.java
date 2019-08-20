package basePackage.objectModel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = Profession.class)
public interface IProfession {
    String getProfession ();
}
