/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * A class of Entities in locations
 * Contains instance variables common to both animals and humans
 */

package Entities;
import CustomExceptions.*;

public abstract class Entity {

    //Instance Variables

    String gender;
    int age;
    String bodyType;
    private enum possibleBodyTypes {AVERAGE,ATHLETIC,OVERWEIGHT,UNSPECIFIED}

    //Constructors
    /**
     * Default constructor for Entities
     */

    public Entity() {

    }

    //Public Methods

    //Accessors

    /**
     * Gets the age of the Entity
     * @return   Returns the age of the Entity
     */

    public int getAge() {
        return this.age;
    }

    /**
     * Gets the body type of the Entity
     * @return   Returns the body type of the Entity
     */

    public String getBodyType() {
        return this.bodyType;
    }

    /**
     * Gets the gender of the Entity
     * @return   Returns the gender of the Entity
     */

    public String getGender() {
        return this.gender;
    }

    //Mutators

    /**
     * Sets the gender of the Entity
     * @param    gender          Gender to be set
     * @param    lineCount       Currently read line
     * @throws   InvalidCharacteristicException  Defaults to unknown when invalid characteristic provided
     */

    public void setGender(String gender, int lineCount) {
        try {
            if (!(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("unknown"))) { //accounting for arbitrary genders, sets to unknown if not male or female
                this.gender = "unknown";
            } else {
                this.gender = gender;
            }
            try{ //throws an exception if an int is provided for gender field
                int testInt = Integer.parseInt(gender);
                throw new Exception();
            }catch (NumberFormatException e) {
                //do nothing if it fails to parse to int
            }catch(Exception e){
                System.out.println("WARNING: invalid characteristic in scenarios file in line " + lineCount);
            }
        } catch (InvalidCharacteristicException e) { //throws an exception if an invalid gender is provided
            System.out.println("WARNING: invalid characteristic in scenarios file in line " + lineCount);
        }
    }

    /**
     * Sets the age of the Entity
     * @param    age            Age to be set
     * @param    lineCount      Currently read line
     */

    public void setAge(int age, int lineCount) {
        this.age = age;
    }

    /**
     * Sets the body type of the Entity
     * @param    bodyType        Body type to be set
     * @param    lineCount       Currently read line
     * @throws   InvalidCharacteristicException  Defaults to unspecified when body type not accepted
     */
     
    public void setBodyType(String bodyType, int lineCount) {
        boolean bodyTypeAllowed = false;
        try {
            for (possibleBodyTypes bodyTypeTest: possibleBodyTypes.values()) { //checks if body type is accepted
                if (bodyTypeTest.name().equalsIgnoreCase(bodyType)) {
                    bodyTypeAllowed = true;
                }
            }
            if (bodyTypeAllowed) {
                this.bodyType = bodyType;
            } else {
                this.bodyType = "unspecified";
                throw new InvalidCharacteristicException(); //if not accepted, throw an exception and set to unspecified
            }
        } catch (InvalidCharacteristicException e) {
            System.out.println("WARNING: invalid characteristic in scenarios file in line " + lineCount);
        }
    }

}
