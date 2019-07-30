package basePackage.objectModel;

import basePackage.exeptions.NotCorrectNameExeption;
import basePackage.exeptions.NotFoundNameException;

import java.util.*;

public class Profession implements IProfession {
    private String professionName;
    private int id;
    private static int count = 0;

    public Profession(String professionName) {
        try {
            if (professionName == null) throw new NotFoundNameException();
        } catch (NotFoundNameException e) {
            e.printStackTrace();
        }
        this.professionName = professionName;
        id = ++count;
    }

    @Override
    public String getProfession() {
        if (professionName == null || professionName.matches("[a-zA-ZА-Яа-я]") || professionName.length() == 0)
            return "Безработный";
        else return professionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profession profession = (Profession) o;
        return professionName.equals(profession.professionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professionName, id);
    }

    @Override
    public String toString() {
        return getProfession();
    }

    public static class Education {
        private String speciality;

        Education(String speciality) {
            this.speciality = speciality;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public void EducationLevel(String educationLevel) {
            class LevelsEducation {
                private String typeEducation;
                private Set<String> a = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList("Среднее общее ", "Полное среднее ", "Высшее ")));

                LevelsEducation(String typeEducation) {
                    this.typeEducation = typeEducation;
                    if (!a.contains(typeEducation)) throw new NotCorrectNameExeption(typeEducation, "Образование может быть только трех типов:\"Среднее общее\", \"Полное среднее\", \"Высшее\"");;
                }


            }
            LevelsEducation myEducationLevel = new LevelsEducation(educationLevel){
                @Override
                public String toString(){
                return super.typeEducation+"образование";
                }
            };
        }
    }

}
