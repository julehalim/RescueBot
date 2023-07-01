/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * A class of Animals, extends Entity
 * Contains instance variables specific to pets, species and isPet
 * Indicates species of animal and whether they are a pet
 */

package Entities;
import CustomExceptions.*;

public class Animal extends Entity {

    //Instance Variables

    private String species;
    private boolean isPet;
    private enum petSpecies {CAT,DOG,FERRET};

    //Constructors
    /**
     * Default constructor for Entities
     */
     public Animal() {

    }

    /**
     * Constructor for not a pet
     * @param   age                 Age of animal to be set
     * @param   gender              Gender of animal to be set
     * @param   bodyType            Body type of the animal
     * @param   species             Species of the animal
     * @param   lineCount           Line currently being read
     * @throws InvalidCharacteristicException    Thrown if gender is not male, female, or unknown, defaults to unknown
     */

    public Animal(int age, String gender, String bodyType, String species, int lineCount) {
        this.isPet = false;
        super.setGender(gender, lineCount);
        super.setAge(age, lineCount);
        this.species = species;
    }

     /** 
     * Constructor for a possible pet
     * @param   age         Age of animal to be set
     * @param   gender      Gender of animal to be set
     * @param   bodyType    Body type of the animal
     * @param   species     Species of the animal
     * @param   isPet       Boolean indicating if animal is a pet
     * @param   lineCount   Line currently being read
     */

    public Animal(int age, String gender, String bodyType, String species, boolean isPet, int lineCount) {
        this.isPet = isPet;
        super.setGender(gender, lineCount);
        this.species = species;
    }
    
    //Public Methods

    //Accesors

    /**
     * Gets the species of the Animal
     * @return   Returns the animal species
     */

    public String getSpecies() {
        return this.species;
    }

    /**
     * Gets the pet status of the Animal
     * @return   Returns the pet status boolean
     */

    public boolean getPetStatus() {
        return isPet;
    }

    //Mutators

    /**
     * Sets the species of the Animal, defaults to unknown if species not accepted
     * @param    species         Species to be set
     * @param    lineCount       Currently read line
     * @throws   NumberFormatException  Defaults species to unknown if an invalid data type is provided
     */

    public void setSpecies(String species,int lineCount) {
        //if species is not taken into account, changed into default
        try{
            this.species=species;
        }catch(NumberFormatException e){ //if an int is provided instead will throw an exception
            System.out.println("WARNING: invalid characteristic in scenarios file in line " + lineCount);
            this.species = "unknown";
        }
    }

    /**
     * Sets the pet status of the Entity
     * @param    petStatus      Boolean indicating if animal is a pet
     * @param    lineCount      Currently read line
     * @throws   InvalidCharacteristicException  Defaults to not a pet when pet species cannot be a pet but true is said to be a pet
     */

    public void setPetStatus(boolean petStatus, int lineCount) {
        boolean canBePet = false;
        try {
            for (petSpecies speciesTest: petSpecies.values()) {
                if (speciesTest.name().equalsIgnoreCase(this.species)) {
                    canBePet = true;
                }
            }
            if (canBePet) {
                this.isPet = petStatus;
            } else if (canBePet == false && petStatus == true) {
                this.isPet = false;
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineCount); //if not a pet species, but is is a pet, an exception is thrown
            }
        } catch (InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
        }
    }
}
