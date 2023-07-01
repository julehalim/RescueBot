/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * A class of Humans, extending from Entity
 * Has methods to call from parent Entity
 * Contains instance variables specific to humans
 */

package Entities;
import CustomExceptions.*;

public class Human extends Entity {

    //Instance Variables

    String profession;
    private enum possibleProfessions {DOCTOR,CEO,CRIMINAL,HOMELESS,UNEMPLOYED,NONE,SCIENTIST,PRESIDENT,PROFESSOR,STUDENT};
    private enum possibleAgeGroups {BABY,CHILD,ADULT,SENIOR};
    String ageGroup;
    private boolean everPregnant;

    //Constructors

    /**
     * Default constructor
     * All humans are entities
     * Sets everPregnant to default false, indicating that they can never be pregnant otherwise they would be in an Adult Female class
     */

    public Human() {
        everPregnant = false;
    }

    //Public Methods

    //Accessors

    /**
     * Gets the age of the Human
     * @return   Returns the age of the Human with a call to super
     */

    public int getAge() {
        return super.getAge();
    }

    /**
     * Gets the age group of the Human
     * @return   Return a String of the Human's age group
     */

    public String getAgeGroup() {
        return this.ageGroup;
    }

    /**
     * Gets the body type of the Human
     * @return   Return a String of the Human's body type, with a call to super
     */

    public String getBodyType() {
        return super.getBodyType();
    }
    /**
     * Gets the profession of the Human
     * @return   Return a String of the Human's profession
     */

    public String getProfession() {
        return this.profession;
    }

    /**
     * Gets the gender of the Human
     * @return   Return a String of the Human's gender with a call to super
     */

    public String getGender() {
        return super.getGender();
    }
    //Mutators

    /**
     * Sets everPregnant
     * @param    isPregnant     Checks if pregnancy state for any non-adult female is true, which will throw an exception if true
     * @param    lineCount      Line currently being read
     * @throws   InvalidCharacteristicException      When a non-adult female is given true for pregnant
     */

    public void setEverPregnant(boolean isPregnant, int lineCount) {
        try {
            if (isPregnant) {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineCount); //checks if a non adult female is pregnant, if they are, throw an exception
            }
        } catch (InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sets gender
     * @param    gender         Gender provided to be set
     * @param    lineCount      Line currently being read
     * @throws   InvalidCharacteristicException      When a invalid gender is provided, including ints, defautls to unknown
     */

    public void setGender(String gender, int lineCount) {
        super.setGender(gender.toLowerCase(), lineCount);
    }

    /**
     * Sets age
     * @param    age         Age provided to be set
     * @param    lineCount   Line currently being read
     * @throws   InvalidCharacteristicException      When an invalid data type is provided, age is set to a default of 0 with a call to super
     */

    public void setAge(int age, int lineCount) {
        super.setAge(age, lineCount);
    }

    /**
     * Sets body type
     * @param    bodyType        Body type to be set
     * @param    lineCount       Line currently being read
     * @throws   InvalidCharacteristicException      When an invalid body type is provided, defaults to unspecified with a call to super
     */

    public void setBodyType(String bodyType, int lineCount) {
        super.setBodyType(bodyType, lineCount);
    }

    /**
     * Sets age group
     * @param    age         Age provided to set age group
     */

    public void setAgeGroup(int age) {
        int maxBabyAge = 4;
        if (age >= 0 && age <= maxBabyAge) {
            this.ageGroup = "baby";
        }
        int maxChildAge = 16;
        if (age >= maxBabyAge + 1 && age <= maxChildAge) {
            this.ageGroup = "child";
        }
        int maxAdultAge = 68;
        if (age >= maxChildAge + 1 && age <= maxAdultAge) {
            this.ageGroup = "adult";
        }
        if (age > maxAdultAge) {
            this.ageGroup = "senior";
        }
    }

    /**
     * Sets profession
     * @param   profession   Profession to be set
     * @param    lineCount   Line currently being read
     * @throws   InvalidCharacteristicException  When invalid profession is provided, defaults to none
     */

    public void setProfession(String profession, int lineCount) {
        boolean professionAllowed = false;
        try{ //throws an exception if an int is provided for gender field
            int testInt = Integer.parseInt(profession);
            throw new Exception();
        }catch (NumberFormatException e) {
            //do nothing if it fails to parse to int
        }catch(Exception e){
            System.out.println("WARNING: invalid characteristic in scenarios file in line " + lineCount);
        }
        if (this.ageGroup.equalsIgnoreCase("adult")) {
            try {
                for (possibleProfessions professionTest: possibleProfessions.values()) {
                    if (professionTest.name().equalsIgnoreCase(profession)) {
                        professionAllowed = true;
                    }
                }
                if (professionAllowed) {
                    this.profession = profession;
                } else {
                    this.profession = "none"; //checking for arbitrary professions are done in another RescueList method, but calls to this method with an invalid profession will cause an exception
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineCount); //checks for if profession provided is an int is also done in RescueList
                }
            } catch (InvalidCharacteristicException e) {
                System.out.println(e.getMessage());
            }
        } else {
            this.profession = "none";
        }

    }
}
