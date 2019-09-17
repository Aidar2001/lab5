package basePackage.objectModel;

import basePackage.exeptions.NotCorrectNameExeption;
import basePackage.exeptions.NotFoundNameException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.*;

/**
 * It's class professions. It implement Iprofession
 */
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@Getter
public class Profession implements IProfession {
    private static int count = 0;

    @XmlAttribute
    private String profession;

    @XmlAttribute
    private int id;

    public Profession() {
    }

    public Profession(String profession) {
        try {
            if (profession == null) throw new NotFoundNameException();
        } catch (NotFoundNameException e) {
            e.printStackTrace();
        }
        this.profession = profession;
        id = ++count;
    }

    /**
     *
     * @return <code>professionName</code> or string "Unemployed" if <code>professionName</code> not initialized
     */
    @Override
    public String getProfession() {
        if (profession == null || profession.matches("[a-zA-ZА-Яа-я]") || profession.length() == 0) {
            return "Unemployed";
        } else {
            return profession;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profession profession = (Profession) o;
        return this.profession.equals(profession.profession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profession, id);
    }

    @Override
    public String toString() {
        return getProfession();
    }


    @AllArgsConstructor
    @Setter
    @Getter
    public static class Education {
        private String speciality;

        public void EducationLevel(String educationLevel) {
            class LevelsEducation {
                private String typeEducation;
                private Set<String> a = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList("Secondary general education ","Full secondary ","Higher ")));

                LevelsEducation(String typeEducation) {
                    this.typeEducation = typeEducation;
                    if (!a.contains(typeEducation)) throw new NotCorrectNameExeption(typeEducation, "Education can be only of three types: \"General secondary\", \"Full secondary\", \"Higher\"");;
                }


            }
            LevelsEducation myEducationLevel = new LevelsEducation(educationLevel){
                @Override
                public String toString(){
                    return super.typeEducation+"education";
                }
            };
        }
    }

}
