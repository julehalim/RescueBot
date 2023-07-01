import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Class to randomize a list of scenarios
 */

public class Randomizer {

    //Instance Variables

    private ArrayList < Scenario > scenarioList;
    private Random random;
    private enum possibleScenarios {WILDFIRE,FLOOD,EARTHQUAKE,TYPHOON,TSUNAMI};
    private enum possibleProfessions {DOCTOR,CEO,CRIMINAL,HOMELESS,UNEMPLOYED,NONE,SCIENTIST,PRESIDENT,PROFESSOR,STUDENT};
    private enum petSpecies {CAT,DOG,FERRET};
    private enum possibleSpecies {CAT,DOG,FERRET,COW,PIG,BOAR,DEER,MONKEY,GORILLA,PENGUIN,WALLABY,KOALA,SNAKE,COCKATOO,DINGO,PLATYPUS,POSSUM};
    private enum possibleBodyTypes {AVERAGE,ATHLETIC,OVERWEIGHT,UNSPECIFIED};

    //Constructor

    /**
     * Default constructor
     * Initializes instance variables
     */

    public Randomizer() {
        scenarioList = new ArrayList < Scenario > ();
        random = new Random();
    }

    //Public Methods

    /**
     * Creates a randomized list of scenarios
     * @param    howMany     Indicates how many scenarios to create, defaults to 4 to be used in judging random scenarios when no scenario file is provided
     * @return   Returns an ArrayList of scenarios, which are randomized
     */

    public ArrayList < Scenario > scenarioGenerator(int howMany) {
        //0 is passed if user input is not required in knowing how many scenarios to make
        if (howMany < 1) { 
            int defaultHowMany = 4;
            howMany = defaultHowMany;
        }
        for (int i = 0; i < howMany; i++) {
            possibleScenarios[] scenarioTest = possibleScenarios.values();
            String tempScenarioName = scenarioTest[random.nextInt(scenarioTest.length)].name().toLowerCase();
            Scenario newScenario = new Scenario(tempScenarioName);
            int randomMax = 5; //limits number of locations created
            for (int j = 0; j <= random.nextInt(randomMax) + 2; j++) { //+2 makes it at least 2 locations per scenario
                Location randomLocation = locationGenerator(); //creates a new location
                newScenario.addLocation(randomLocation); //adds in the new location
            }
            scenarioList.add(newScenario); //adds in the created scenario into a scenario list
        }
        return scenarioList;
    }

    //Private Methods

    /**
     * Creates a randomized location
     * @return   Returns a randomized location
     */

    private Location locationGenerator() {
        Location locationGenerated;
        String tempLatitude;
        String tempLongitude;
        int longitudeRange = 180; //range of longitudes
        double randomLongitudeValue = random.nextDouble() * longitudeRange; //random longitude
        randomLongitudeValue = Math.round(randomLongitudeValue * 10000d) / 10000d; //rounds the longitude value
        boolean eastOrNo = random.nextBoolean(); //randomly chooses E/W
        String eastWest;
        if (eastOrNo) {
            eastWest = "E";
        } else {
            eastWest = "W";
        }
        tempLongitude = Double.toString(randomLongitudeValue) + " " + eastWest;
        int latitudeRange = 360; //range of latitudes
        double randomLatitudeValue = random.nextDouble() * latitudeRange; //random latitudes
        randomLatitudeValue = Math.round(randomLatitudeValue * 10000d) / 10000d; //rounds the latitude value
        boolean northOrNo = random.nextBoolean(); //randomly choose N/S
        String northSouth;
        if (northOrNo) {
            northSouth = "N";
        } else {
            northSouth = "S";
        }
        tempLatitude = Double.toString(randomLatitudeValue) + " " + northSouth;
        boolean trespassing = random.nextBoolean(); //randomly chooses trespassing state
        locationGenerated = new Location(trespassing, tempLatitude, tempLongitude);
        int entityNumber = 10; //limits number of entities created
        for (int i = 0; i <= random.nextInt(entityNumber) + 1; i++) { //creates a number of entities for the location 
            Entity newEntity = entityGenerator(); //entity created
            locationGenerated.addEntity(newEntity); //entity added
        }
        return locationGenerated;
    }

    /**
     * Creates a randomized entity, from either animal, adult female, or human
     * @return   Returns a randomized entity
     */

    private Entity entityGenerator() {
        Entity entity = null; //initialize an entity
        int entityTypeAnimal = 2; //2 will create an animal
        int randomEntityType = 2; //maximum number of entity types are 2(humans and animals). adult females are considered when humans are created
        int entityType = random.nextInt(randomEntityType) + 1; //randomly select animal or human
        if (entityType == 1) {
            Human human = new Human();
            int randomAge = 125; //artbitrary max age
            int humanAge = random.nextInt(randomAge) + 1;
            int defaultLineCount = 999; //arbitrary line count, will never return an exception because randomizer will only create valid proprties
            human.setAge(humanAge, defaultLineCount);
            human.setAgeGroup(humanAge);
            boolean maleOrNot = random.nextBoolean();
            if (maleOrNot) {
                human.setGender("male", defaultLineCount);
            } else if (!maleOrNot) {
                human.setGender("female", defaultLineCount);
            }
            String tempBodyType;
            possibleBodyTypes[] bodyTypes = possibleBodyTypes.values();
            tempBodyType = bodyTypes[random.nextInt(bodyTypes.length)].name().toLowerCase();
            human.setBodyType(tempBodyType, defaultLineCount);
            if (human.getAgeGroup().equalsIgnoreCase("adult")) { //checks for adult
                String tempProfession;
                possibleProfessions[] professions = possibleProfessions.values();
                tempProfession = professions[random.nextInt(professions.length)].name().toLowerCase();
                human.setProfession(tempProfession, defaultLineCount);
            } else if (!human.getAgeGroup().equalsIgnoreCase("adult")) { //checks if not adult
                human.setProfession("none", defaultLineCount);
            }
            if (human.getAgeGroup().equalsIgnoreCase("adult") && human.getGender().equalsIgnoreCase("female")) { //checks if it is a adult female
                AdultFemale adultFemale = new AdultFemale(human);
                boolean isPregnant = random.nextBoolean();
                adultFemale.setPregnancy(isPregnant);
                entity = adultFemale;
            } else { //checks if non-adult female
                human.setEverPregnant(false, defaultLineCount);
                entity = human;
            }
        } else if (entityType == entityTypeAnimal) { //checks if animal is randomized
            Animal animal = new Animal();
            int defaultLineCount = 999; //arbitrary line count, will never return an exception because randomizer will only create valid proprties
            String tempSpecies;
            possibleSpecies[] species = possibleSpecies.values();
            tempSpecies = species[random.nextInt(species.length)].name().toLowerCase();
            animal.setSpecies(tempSpecies,0);
            boolean canBePet = false;
            for (petSpecies speciesTest: petSpecies.values()) {
                if (speciesTest.name().equalsIgnoreCase(tempSpecies)) {
                    canBePet = true;
                }
            }
            int petCount = 2;
            int randomPetOrNo = random.nextInt(petCount) + 1;
            if (randomPetOrNo == 1 && canBePet) {
                animal.setPetStatus(true, defaultLineCount);
            } else if (randomPetOrNo == petCount) {
                animal.setPetStatus(false, defaultLineCount);
            }
            int petAge = random.nextInt(100) + 1;
            animal.setAge(petAge, defaultLineCount);

            String tempBodyType;
            possibleBodyTypes[] bodyTypes = possibleBodyTypes.values();
            tempBodyType = bodyTypes[random.nextInt(bodyTypes.length)].name().toLowerCase();
            animal.setBodyType(tempBodyType, defaultLineCount);
            int genderCount = 2;
            int animalGender = random.nextInt(genderCount) + 1;
            if (animalGender == 1) {
                animal.setGender("male", defaultLineCount);
            } else if (animalGender == genderCount) {
                animal.setGender("female", defaultLineCount);
            }
            entity = animal;
        }
        return entity;
    }
}
