import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Class reading in and storing a list of scenarios
 * Reads in files and converts into a scenario list
 */

public class RescueList {

    //Instance Variables

    private Menus menu;
    private ArrayList < Scenario > scenarioList;
    private ArrayList < String > columnNames;

    //Constructor

    /**
     * Default constructor
     * Initializes instance variables
     */

    public RescueList() {
        scenarioList = new ArrayList < Scenario > ();
        columnNames = new ArrayList < String > ();
        menu = new Menus();
    }

    //Public Methods

    //Accessors

    /**
     * Gets the stored list of scenarios
     * @return   Returns an ArrayList of scenarios
     */

    public ArrayList < Scenario > getScenarioList() {
        return this.scenarioList;
    }

    //Mutators

    /**
     * Sets the scenario list using the scenario list given
     * @param   scenarioList    The ArrayList of scenarios that is given to be set
     */

    public void randomizedScenarios(ArrayList < Scenario > scenarioList) {
        this.scenarioList = scenarioList;
    }

    //Other Public Methods

    /**
     * Reads the file into an ArrayList of scenarios, scenarioList
     * @param   file    File to be read
     * @throws  InvalidDataFormatException          Thrown when a row is erroneous
     * @throws  NumberFormatException               Thrown when a invalid field is provided
     * @throws  InvalidCharacteristicException      Thrown when a not accepted field is provided
     */

    public void readFile(File file) {
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(file); //create new scanner to read file
            if (fileScanner.hasNextLine()) { //reads first line (columns)
                String columnName = fileScanner.nextLine();
                String[] columnNameSplit = columnName.split(","); //splits first line into arraylist by separating with ","
                for (String column: columnNameSplit) {
                    columnNames.add(column);
                }
            }
            fileScanner.useDelimiter(","); //each field is now split by separating where "," is
            int lineCount = 1;
            while (fileScanner.hasNextLine()) { //reads until no more lines
                ArrayList < String > currentBody = new ArrayList < String > ();
                lineCount += 1;
                String currentRead = fileScanner.nextLine();
                String[] currentReadSplit = currentRead.split(",", -1);
                for (String field: currentReadSplit) {
                    currentBody.add(field);
                }
                try {
                    if (currentBody.size() != columnNames.size()) { //if column size is incorrect, skips and throws exception
                        throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + lineCount);
                    }
                } catch (InvalidDataFormatException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                if (currentBody.get(0).toLowerCase().contains("scenario")) {
                    Scenario newScenario = new Scenario(currentBody.get(0).substring(currentBody.get(0).indexOf(":") + 1));
                    scenarioList.add(newScenario);
                }
                if (currentBody.get(0).toLowerCase().contains("location")) {
                    String longitude = "UNKNOWN";
                    String latitude = "UNKNOWN";
                    boolean trespassing = false;
                    try {
                        longitude = currentBody.get(0).substring(currentBody.get(0).indexOf(":") + 1, currentBody.get(0).indexOf(";"));
                        latitude = currentBody.get(0).substring(currentBody.get(0).indexOf(";") + 1, currentBody.get(0).lastIndexOf(";"));
                        Double longitudeDouble = Double.parseDouble(longitude.substring(0, longitude.indexOf(" ")));
                        Double latitudeDouble = Double.parseDouble(latitude.substring(0, latitude.indexOf(" ")));
                        int latitudeRange = 360;
                        int longitudeRange = 180;
                        try { //checks for ranges of longitudes and latitudes
                            if (longitudeDouble > longitudeRange || longitudeDouble < 0 || latitudeDouble > latitudeRange || latitudeDouble < 0) {
                                if (longitudeDouble > longitudeRange || longitudeDouble < 0) {
                                    longitude = "UNKNOWN";
                                }
                                if (latitudeDouble > latitudeRange || latitudeDouble < 0) {
                                    latitude = "UNKNOWN";
                                }
                                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineCount);
                            }
                        } catch (InvalidCharacteristicException e) {
                            System.out.println(e.getMessage());
                        }
                        if (currentBody.get(0).toLowerCase().contains("legal")) {
                            trespassing = false;
                        } else if (currentBody.get(0).toLowerCase().contains("trespassing")) {
                            trespassing = true;
                        } else { //defaults to not trespassing and throws an exception if an invalid field is provided
                            trespassing = false;
                            throw new NumberFormatException("WARNING: invalid number format in scenarios file in line " + lineCount);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(e.getMessage());
                    }
                    Location newLocation = new Location(trespassing, longitude, latitude); //creates new location to read
                    scenarioList.get(scenarioList.size() - 1).addLocation(newLocation);
                }
                if (currentBody.get(0).toLowerCase().contains("human")) {
                    Human temp = new Human();
                    if (currentBody.get(1).toLowerCase().contains("female")) {
                        addFemale(currentBody, temp, lineCount);
                    } else if (currentBody.get(1).toLowerCase().contains("male")) {
                        addMale(currentBody, temp, lineCount);
                    } else if (!(currentBody.get(1).toLowerCase().contains("male")) && !(currentBody.get(1).toLowerCase().contains("female"))) { //checks for arbitrary genders; if an int is provided, the call to add invalid gender to the entity will thrown an exception
                        addNeitherMaleOrFemale(currentBody, temp, lineCount);
                    }
                }
                if (currentBody.get(0).toLowerCase().contains("animal".toLowerCase())) {
                    addAnimal(currentBody, lineCount);
                } else if (!(currentBody.get(0).toLowerCase().contains("animal") || currentBody.get(0).toLowerCase().contains("human") || currentBody.get(0).toLowerCase().contains("location") || currentBody.get(0).toLowerCase().contains("scenario"))) { //if given field is not valid for entities, scenarios, or locations
                    try {
                        throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineCount);
                    } catch (InvalidCharacteristicException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("scenario file not found");
            menu.helpMenu();
        }
        fileScanner.close();
    }

    //Private Methods

    /**
     * Adds females into the scenarioList
     * @param   currentBody     The current row being checked
     * @param   temp            The object Entity created when first reading the file for Humans
     * @param   lineCount       The line number of the row currently being read
     * @throws  InvalidCharacteristicException   Thrown in age is less than 0
     */

    private void addFemale(ArrayList < String > currentBody, Human temp, int lineCount) {
        int tempAge = 0;
        try {
            tempAge = Integer.parseInt(currentBody.get(2));
            if (tempAge < 0) {
                tempAge = 0;
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineCount);
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("WARNING: invalid characteristic in scenarios file in line " + lineCount);
        }
        AdultFemale tempAdultFemale;
        int bodyTypePosition = 3; //index of field for body type
        String tempBodyType = currentBody.get(bodyTypePosition);
        int professionPosition = 4; //index of field for profession
        String tempProfession = currentBody.get(professionPosition);
        int lowerAdultRange = 17;
        int higherAdultRange = 68;
        if (tempAge >= lowerAdultRange && tempAge <= higherAdultRange) {
            tempAdultFemale = new AdultFemale();
            boolean isPregnant;
            int pregnantPosition = 5; //index of field for pregnancy
            if (currentBody.get(pregnantPosition).equalsIgnoreCase("true")) {
                isPregnant = true;
            } else {
                isPregnant = false;
            }
            tempAdultFemale.setBodyType(tempBodyType, lineCount);
            tempAdultFemale.setAgeGroup(tempAge);
            tempAdultFemale.setAge(tempAge, lineCount);
            tempAdultFemale.setProfession(tempProfession, lineCount);
            tempAdultFemale.setPregnancy(isPregnant);
            scenarioList.get(scenarioList.size() - 1).getLocationArray().get(scenarioList.get(scenarioList.size() - 1).getLocationArray().size() - 1).addEntity(tempAdultFemale);
        } else {
            temp = new Human();
            temp.setGender(currentBody.get(1).toLowerCase(), lineCount);
            temp.setBodyType(tempBodyType, lineCount);
            temp.setAge(tempAge, lineCount);
            temp.setAgeGroup(tempAge);
            temp.setProfession(tempProfession, lineCount);
            boolean isPregnant;
            int pregnantPosition = 5; //index of field for pregnancy
            if (currentBody.get(pregnantPosition).equalsIgnoreCase("true")) {
                isPregnant = true;
            } else {
                isPregnant = false;
            }
            temp.setEverPregnant(isPregnant, lineCount); //set entity to never be able to be pregnant, otherwise an exception will be thrown in the call to setEverPregnant
            scenarioList.get(scenarioList.size() - 1).getLocation(scenarioList.get(scenarioList.size() - 1).getLocationArray().size() - 1).addEntity(temp); //adds entity to a list
        }
    }

    /**
     * Adds males into the scenarioList
     * @param   currentBody     The current row being checked
     * @param   temp            The object Entity created when first reading the file for Humans
     * @param   lineCount       The line number of the row currently being read
     * @throws  NumberFormatException   Thrown in age is less than 0
     */

    private void addMale(ArrayList < String > currentBody, Human temp, int lineCount) {
        temp = new Human();
        temp.setGender(currentBody.get(1).toLowerCase(), lineCount);
        int bodyTypePosition = 3;//index of field for body type
        temp.setBodyType(currentBody.get(bodyTypePosition).toLowerCase(), lineCount);
        int tempAge = 0;
        int agePosition = 2;//index of field for age
        try {
            tempAge = Integer.parseInt(currentBody.get(agePosition)); //checks if age is less than 0 and will throw an exception if it is, and set the default age to 0
            if (tempAge < 0) {
                tempAge = 0;
                throw new NumberFormatException("WARNING: invalid number format in scenarios file in line " + lineCount);
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("WARNING: invalid number format in scenarios file in line " + lineCount);
        }
        temp.setAge(tempAge, lineCount);
        temp.setAgeGroup(tempAge);
        int professionPosition = 4;//index of field for profession
        temp.setProfession(currentBody.get(professionPosition), lineCount);
        scenarioList.get(scenarioList.size() - 1).getLocation(scenarioList.get(scenarioList.size() - 1).getLocationArray().size() - 1).addEntity(temp); //adds entity to a list
    }

    /**
     * Adds genders not male or female into the scenarioList
     * @param   currentBody     The current row being checked
     * @param   temp            The object Entity created when first reading the file for Humans
     * @param   lineCount       The line number of the row currently being read
     * @throws  NumberFormatException   Thrown if age is less than 0
     * @throws  RuntimeException        Thrown for any invalid inputs
     */

    private void addNeitherMaleOrFemale(ArrayList < String > currentBody, Human temp, int lineCount) {
        temp = new Human();
        temp.setGender(currentBody.get(1).toLowerCase(), lineCount); //will pass to human setGender method, which defaults it to unknown if an error occurs
        int bodyTypeAge = 3;//index of field for body type
        temp.setBodyType(currentBody.get(bodyTypeAge).toLowerCase(), lineCount);
        int tempAge = 0;
        try {
            int agePosition = 2;//index of field for age
            tempAge = Integer.parseInt(currentBody.get(agePosition));
            if (tempAge < 0) {
                tempAge = 0;
                throw new NumberFormatException("WARNING: invalid number format in scenarios file in line " + lineCount);
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("WARNING: invalid number format in scenarios file in line " + lineCount);
        }
        temp.setAge(tempAge, lineCount);
        temp.setAgeGroup(tempAge);
        int professionPosition = 4;//index of field for profession
        temp.setProfession(currentBody.get(professionPosition), lineCount);
        scenarioList.get(scenarioList.size() - 1).getLocation(scenarioList.get(scenarioList.size() - 1).getLocationArray().size() - 1).addEntity(temp); //adds entity to a list
    }

    /**
     * Adds animals into the scenarioList
     * @param   currentBody        The current row being checked
     * @param   lineCount          The line number of the row currently being read
     * @throws  InvalidCharacteristicException  Thrown if an int is provided as a species
     */

    private void addAnimal(ArrayList < String > currentBody, int lineCount) {
        Animal tempAnimal = new Animal();
        boolean integerAnimal = false;
        int speciesPosition = 6;//index of field for species
        try {
            int testCase = Integer.parseInt(currentBody.get(speciesPosition)); //checks if int is provided, doing nothing if it is not as it throws a new Exception instead
            throw new InvalidCharacteristicException();
        } catch (InvalidCharacteristicException e) {
            System.out.println("WARNING: invalid characteristic in scenarios file in line " + lineCount);
            integerAnimal = true;
        } catch (Exception e) {

        }
        if (!integerAnimal) { //sets values depending on wheter int is provided or proper species is provided
            tempAnimal.setSpecies(currentBody.get(speciesPosition),lineCount);
        } else if (integerAnimal) {
            tempAnimal.setSpecies("unknown",lineCount);
        }
        tempAnimal.setGender(currentBody.get(1), lineCount);
        int bodyTypeAge = 3;//index of field for body type
        tempAnimal.setBodyType(currentBody.get(bodyTypeAge), lineCount);
        int agePosition = 2;//index of field for age
        tempAnimal.setAge(Integer.parseInt(currentBody.get(agePosition)), lineCount);
        boolean isPet = false;
        int petPosition = 7;//index of field for pet status
        if (currentBody.get(petPosition).equalsIgnoreCase("true")) {
            isPet = true;
        }
        tempAnimal.setPetStatus(isPet, lineCount); //exception will be thrown if a non-pet species is a pet
        scenarioList.get(scenarioList.size() - 1).getLocation(scenarioList.get(scenarioList.size() - 1).getLocationArray().size() - 1).addEntity(tempAnimal); //adds entity to a list
    }
}
