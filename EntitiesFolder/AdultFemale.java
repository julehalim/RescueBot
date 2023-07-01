/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * A special class adult females, who are able to get pregnant, extends Human
 * Contains methods calling to its parent Human
 * Has a special instance variable to indicate pregnancy state
 */

package Entities;
import CustomExceptions.*;

public class AdultFemale extends Human {

    //Instance Variables

    private boolean isPregnant;

    //Constructors
    /**
     * Default constructor
     * All adult females are Human entities that are female and adult
     */
    public AdultFemale() {
        this.gender = "female";
        this.ageGroup = "adult";
    }
    /**
     * Copy constructor
     * Creates an adult Female from a Human
     * @param human       The entity Human to be copied from
     */
    public AdultFemale(Human human) {
        this.gender = "female";
        this.ageGroup = "adult";
        this.age = human.getAge();
        this.bodyType = human.getBodyType();
        this.profession = human.getProfession();
    }

    //Public Methods

    //Accesors

    /**
     * Accesses pregnancy state
     * @return   Returns a boolean indicating pregnancy state
     */

    public boolean getPregnant() {
        return this.isPregnant;
    }

    //Mutators

    /**
     * Sets the age of Adult Female, calls super to Human
     * @param    age         Age of Adult Female
     * @param    lineCount   Line currently being read
     * @throws   NumberFormatException   Thrown if line age is less than 0, actual throw is found in Entity
     */

    public void setAge(int age, int lineCount) {
        super.setAge(age, lineCount);
    }

    /**
     * Sets the gender of Adult Female, calls super to Human, but will always default to Female
     * @param  gender        Gender to be set
     * @param  lineCount     Line currently being read
     * @throws InvalidCharacteristicException    Thrown if gender is not male, female, or unknown, defaults to unknown, actual throw is in Entity
     */

    public void setGender(String gender, int lineCount) {
        super.setGender(gender, lineCount);
    }

    /**
     * Sets the body type of Adult Female, calls super to Human
     * @param  bodyType      Body type to be set
     * @param  lineCount     Line currently being read
     * @throws InvalidCharacteristicException    Thrown if body type is not accepted, defaults to unspecified, actual throw in Entity
     */

    public void setBodyType(String bodyType, int lineCount) {
        super.setBodyType(bodyType, lineCount);
    }

    /**
     * Sets the profession of Adult Female, calls super to Human
     * @param  profession     Profession type to be set
     * @param  lineCount      Line currently being read
     * @throws InvalidCharacteristicException    Thrown if profession is not accepted, defaults to none, actual throw is in Entity
     */

    public void setProfession(String profession, int lineCount) {
        super.setProfession(profession, lineCount);
    }

    /**
     * Sets the pregnancey state of Adult Female
     * @param  isPregnant    Pregnancy state to be set
     */

    public void setPregnancy(boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    /**
     * Sets the pregnancey state of Adult Female
     * @param  age    Age of Adult Female, placed into age group in call to super
     */

    public void setAgeGroup(int age) {
        super.setAgeGroup(age);
    }

}
