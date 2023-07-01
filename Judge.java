import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.util.InputMismatchException;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Performs judgements from bot
 * Allows user to perform judgements
 */

public class Judge {
    private Audit audit;
    private ArrayList < String > totalCountStatistic;
    private RescueBot rescueBot;
    private Menus menu;

    //Constructors

    /**
     * Default Constructor
     * Initializes instance variables
     */

    public Judge() {
        audit = new Audit();
        totalCountStatistic = new ArrayList < String > ();
        rescueBot = new RescueBot();
        menu = new Menus();
    }

    //Public Methods

    //Accessors

    /**
     * Returns the statistic obtained from judging either by user or bot
     * @return Returns a statistic to be used
     */

    public ArrayList < String > getTotalCountStatistic() {
        return totalCountStatistic;
    }

    //Public Mutators

    /**
     * Adds the total number of times characteristic from an entity is saved
     * Obtained from an entity in all locations in a scenario
     * @param   consideredCharacteristic       Characteristic extracted from entity
     */

    public void addChosenCount(String consideredCharacteristic) {
        int statisticIndex = 0;
        for (int i = 0; i < totalCountStatistic.size(); i++) {
            if (consideredCharacteristic.equalsIgnoreCase(totalCountStatistic.get(i))) { //searches for index of statistic when it is already included
                statisticIndex = i;
                break;
            }
        }
        int updateOverCount = Integer.parseInt(totalCountStatistic.get(statisticIndex + 1)) + 1;
        try {
            totalCountStatistic.set(statisticIndex + 1, String.valueOf(updateOverCount));
        } catch (Exception e) { //checks for exceptions in adding values to statistic
            System.out.println("some error in adding to numerator");
        }
    }

    //Other Public Methods

    /**
     * Executes an algorithm judgement
     * @param scenarioList                      A list of scenarios to be judged
     * @param logFile                           File to save statistics into
     * @param judge                             Pass into Audit audit to perform statistics
     * @param logFileProvided                   Used to check if log file was provided by user
     * @param logFileExists                     Used to check if a user-provided log file exists. If not, exits with an error message
     */

    public void botJudge(ArrayList < Scenario > scenarioList, File logFile, Judge judge, boolean logFileProvided, boolean logFileExists) {
        int totalAge = 0;
        int totalEntities = 0;
        int totalScenarios = 0;
        for (int i = 0; i < scenarioList.size(); i++) {
            totalScenarios += 1;
            for (int j = 0; j < scenarioList.get(i).getLocationArray().size(); j++) {
                for (int k = 0; k < scenarioList.get(i).getLocationArray().get(j).getEntityArray().size(); k++) {
                    if (scenarioList.get(i).getLocationArray().get(j).returnTrespassing()) {
                        addToTotalCount("trespassing");
                    } else {
                        addToTotalCount("legal");
                    }
                    Entity currentCheckEntity = scenarioList.get(i).getLocationArray().get(j).getEntityArray().get(k);
                    if (currentCheckEntity instanceof AdultFemale) {
                        checkAdultFemaleJudge(currentCheckEntity, false);
                    } else if (currentCheckEntity instanceof Human) {
                        checkHumanJudge(currentCheckEntity, false);
                    } else if (currentCheckEntity instanceof Animal) {
                        checkAnimalJudge(currentCheckEntity, false);
                    }
                }
            }
            Location tempLocation;
            tempLocation = rescueBot.decide(scenarioList.get(i)); //calls to RescueBot for decision algorithm
            for (int m = 0; m < tempLocation.getEntityArray().size(); m++) {
                if (tempLocation.getEntityArray().get(m) instanceof Human) {
                    totalAge += tempLocation.getEntityArray().get(m).getAge();
                    totalEntities += 1;
                }
            }
            audit.makeStatistic(tempLocation, audit, judge);
        }
        audit.saveAudit(logFile, totalCountStatistic, "robotRun", logFileProvided, logFileExists, totalScenarios, totalAge, totalEntities); //always saves audit as bot run
        audit.printAudit(totalCountStatistic, scenarioList.size() - 1, false); //prints out the resulting audit
    }

    /**
     * Allows user to judge scenarios
     * @param userConsent                           Checks if user provided consent to save judgements
     * @param scenarioList                          A list of scenarios to be judged
     * @param logFile                               File to save statistics into
     * @param scanner                               Reads in user input
     * @param judge                                 Pass into Audit audit to perform statistics
     * @param logFileProvided                       Used to check if log file was provided by user
     * @param logFileExists                         Used to check if a user-provided log file exists. If not, exits with an error message
     * @param containsScenario                      Used to check if a user provided a scenario
     * @throws  InvalidInputException               Thrown if user passes in an invalid input
     * @throws  InputMismatchException              Thrown if user provides an input that is not accepted
     */

    public void judgeScenario(String userConsent, ArrayList < Scenario > scenarioList, File logFile, Scanner scanner, Judge judge, boolean logFileProvided, boolean logFileExists, boolean containsScenario) {
        int totalScenarios = 0;
        int totalAge = 0;
        int totalEntities = 0;
        for (int i = 0; i < scenarioList.size(); i++) {
            totalScenarios += 1;
            System.out.println("======================================");
            System.out.println("# Scenario: " + scenarioList.get(i).getScenarioName());
            System.out.println("======================================");
            for (int j = 0; j < scenarioList.get(i).getLocationArray().size(); j++) { //prints and considers location properties
                System.out.printf("[%d] Location: %s, %s%n", j + 1, scenarioList.get(i).getLocationArray().get(j).getLocationLatitude(), scenarioList.get(i).getLocationArray().get(j).getLocationLongitude());
                System.out.printf("Trespassing: %s%n", scenarioList.get(i).getLocationArray().get(j).getTrespassingString());
                System.out.println(scenarioList.get(i).getLocationArray().get(j).getEntityArray().size() + " Characters: ");
                for (int k = 0; k < scenarioList.get(i).getLocationArray().get(j).getEntityArray().size(); k++) {
                    if (scenarioList.get(i).getLocationArray().get(j).returnTrespassing()) {
                        judge.addToTotalCount("trespassing"); //add trespassing as a characteristic
                    } else {
                        judge.addToTotalCount("legal"); //add legal as a characteristic
                    }
                    Entity currentCheckEntity = scenarioList.get(i).getLocationArray().get(j).getEntityArray().get(k);
                    if (currentCheckEntity instanceof AdultFemale) {
                        checkAdultFemaleJudge(currentCheckEntity, true);
                    } else if (currentCheckEntity instanceof Human) {
                        checkHumanJudge(currentCheckEntity, true);
                    } else if (currentCheckEntity instanceof Animal) {
                        checkAnimalJudge(currentCheckEntity, true);
                    }
                }
            }
            int inputInt = 0;
            int errorOrNot = 0;
            while (true) { //loops until a valid input is provided
                if (errorOrNot == 0) {
                    System.out.println("To which location should RescueBot be deployed?"); //get user input on which location to save
                    System.out.print("> ");
                }
                try {
                    inputInt = scanner.nextInt();
                    scanner.nextLine();
                    if (inputInt <= 0 || inputInt > scenarioList.get(i).getLocationArray().size()) {
                        errorOrNot = 1;
                        throw new InvalidInputException("Invalid response! To which location should RescueBot be deployed?");
                    } else {
                        break;
                    }
                } catch (InvalidInputException e) {
                    System.out.printf("%s%n", e.getMessage());
                    System.out.print("> ");
                    errorOrNot = 1;
                    continue;
                } catch (InputMismatchException e) {
                    System.out.printf("Invalid response! To which location should RescueBot be deployed?%n");
                    System.out.print("> ");
                    errorOrNot = 1;
                    scanner.nextLine();
                    continue;
                }
            }
            for (int m = 0; m < scenarioList.get(i).getLocation(inputInt - 1).getEntityArray().size(); m++) {
                if (scenarioList.get(i).getLocation(inputInt - 1).getEntityArray().get(m) instanceof Human) {
                    totalAge += scenarioList.get(i).getLocation(inputInt - 1).getEntityArray().get(m).getAge(); //adds total age of humans in saved location
                    totalEntities += 1; //adds total number of humans in saved location
                }
            }
            audit.makeStatistic(scenarioList.get(i).getLocation(inputInt - 1), audit, judge);
            int everyThree = 3;
            if (((i + 1) % everyThree == 0 && i != 0) || (i == scenarioList.size() - 1)) { //stops every 3 locations to ask if user wants to continue
                if (!containsScenario) { //if no scenario is given, number of randomized scenarios will keep on adding until user wants to stop
                    Randomizer randomizer = new Randomizer();
                    RescueList tempRescueList = new RescueList();
                    tempRescueList.randomizedScenarios(randomizer.scenarioGenerator(3)); 
                    scenarioList.addAll(tempRescueList.getScenarioList());
                }
                audit.printAudit(totalCountStatistic, i, false);
                if (i == scenarioList.size() - 1) {
                    if (userConsent.equalsIgnoreCase("yes")) {
                        audit.saveAudit(logFile, totalCountStatistic, "userRun", logFileProvided, logFileExists, totalScenarios, totalAge, totalEntities); //saving to log file, also includes checking if log file is provided but does not exist
                    }
                    menu.thatsAll(scanner, false);
                }
                if (!(i == scenarioList.size() - 1)) {
                    String userContinue = "";
                    while (true) {
                        System.out.println("Would you like to continue? (yes/no)");
                        System.out.print("> ");
                        try {
                            userContinue = scanner.nextLine();
                            if (userContinue.equalsIgnoreCase("yes")) {
                                break;
                            } else if (userContinue.equalsIgnoreCase("no")) {
                                if (userConsent.equalsIgnoreCase("yes")) { //saves only if user consents
                                    audit.saveAudit(logFile, totalCountStatistic, "userRun", logFileProvided, logFileExists, totalScenarios, totalAge, totalEntities);//saving to log file, also includes checking if log file is provided but does not exist
                                }
                                i = scenarioList.size() + 1;
                                menu.thatsAll(scanner, false);
                                break;
                            } else {
                                throw new InvalidInputException();
                            }
                        } catch (InvalidInputException e) {
                            System.out.print("Invalid response! ");
                            continue;
                        }
                    }
                }
            }
        }
    }

    //Private Methods

    //Private Mutators

    /**
     * Adds the total number of times characteristic from an entity occurs
     * Obtained from an entity in all locations in a scenario
     * @param String consideredCharacteristic       Characteristic extracted from entity
     */

    private void addToTotalCount(String consideredCharacteristic) {
        boolean inStatistic = false;
        int statisticIndex = 0;
        for (int i = 0; i < totalCountStatistic.size(); i++) {
            if (consideredCharacteristic.equalsIgnoreCase(totalCountStatistic.get(i))) {
                inStatistic = true;
                statisticIndex = i;
                break;
            }
        }
        if (!inStatistic) { //adds to statistic has not been considered
            totalCountStatistic.add(consideredCharacteristic);
            totalCountStatistic.add("0");
            totalCountStatistic.add("1");
        }
        int indexTotal = 2;
        if (inStatistic) { //replaces if a characteristic is already in a statistic
            int updateTotalCount = Integer.parseInt(totalCountStatistic.get(statisticIndex + indexTotal)) + 1;
            int statisticReplacePosition = 2;
            totalCountStatistic.set(statisticIndex + statisticReplacePosition, String.valueOf(updateTotalCount));
        }
    }

    //Other Private Methods

    /**
     * Prints out and performs statistics for animals
     * @param currentCheckEntity     Currently entity to perform statistics on
     * @param needToPrint            Checks if animal characteristics need to be printed, meaning that it is an user judging
     */

    private void checkAnimalJudge(Entity currentCheckEntity, boolean needToPrint) { //judges and audits animal
        Animal currentCheckAnimal = (Animal) currentCheckEntity;
        String species = currentCheckAnimal.getSpecies();
        if (currentCheckAnimal.getPetStatus()) {
            if (needToPrint) { //indicates if it is a bot audit or user audit
                System.out.printf("- %s is pet%n", species);
            }
            addToTotalCount("animal");
            addToTotalCount("pet");
            if(!species.equalsIgnoreCase("unknown")){
                addToTotalCount(species);
            }

        } else {
            if (needToPrint) {
                System.out.printf("- %s%n", species);
            }
            addToTotalCount("animal");
            if(!species.equalsIgnoreCase("unknown")){
                addToTotalCount(species);
            }

        }
    }

    /**
     * Prints out and performs statistics for humans
     * @param currentCheckEntity     Currently entity to perform statistics on
     * @param needToPrint            Checks if animal characteristics need to be printed, meaning that it is an user judging
     */

    private void checkHumanJudge(Entity currentCheckEntity, boolean needToPrint) { //judges and audits humans
        Human currentCheckHuman = (Human) currentCheckEntity;
        String bodyType = currentCheckHuman.getBodyType();
        String ageCategory = currentCheckHuman.getAgeGroup();
        String gender = currentCheckHuman.getGender();
        if (ageCategory.equalsIgnoreCase("adult")) {
            String profession = currentCheckHuman.getProfession();
            if (needToPrint) { //indicates if it is a bot audit or user audit
                System.out.printf("- %s %s %s %s%n", bodyType, ageCategory, profession, gender);
            }

            if (!bodyType.equalsIgnoreCase("unspecified")) {
                addToTotalCount(bodyType);
            }
            addToTotalCount("human");
            addToTotalCount(ageCategory);
            if (!profession.equalsIgnoreCase("none")) {
                addToTotalCount(profession);
            }
            if (!gender.equalsIgnoreCase("unknown")) {
                addToTotalCount(gender);
            }
        } else if (!ageCategory.equalsIgnoreCase("adult")) {
            if (needToPrint) {
                System.out.printf("- %s %s %s%n", bodyType, ageCategory, gender);
            }
            if (!bodyType.equalsIgnoreCase("unspecified")) {
                addToTotalCount(bodyType);
            }
            addToTotalCount("human");
            addToTotalCount(ageCategory);
            if (!gender.equalsIgnoreCase("unknown")) {
                addToTotalCount(gender);
            }
        }
    }

    /**
     * Prints out and performs statistics for adult females
     * @param currentCheckEntity     Currently entity to perform statistics on
     * @param needToPrint            Checks if animal characteristics need to be printed, meaning that it is an user judging
     */

    private void checkAdultFemaleJudge(Entity currentCheckEntity, boolean needToPrint) {
        AdultFemale currentCheckAdultFemale = (AdultFemale) currentCheckEntity;
        String bodyType = currentCheckAdultFemale.getBodyType();
        String ageCategory = currentCheckAdultFemale.getAgeGroup();
        String gender = currentCheckAdultFemale.getGender();
        String profession = currentCheckAdultFemale.getProfession();
        String pregnant = null;
        if (currentCheckAdultFemale.getPregnant()) { //judges and audits pregnant adult females
            pregnant = "pregnant";
            if (needToPrint) { //indicates if it is a bot audit or user audit
                System.out.printf("- %s %s %s %s %s%n", bodyType, ageCategory, profession, gender, pregnant);
            }

            if (!bodyType.equalsIgnoreCase("unspecified")) {
                addToTotalCount(bodyType);
            }
            addToTotalCount("human");
            addToTotalCount(ageCategory);
            if (!profession.equalsIgnoreCase("none")) {
                addToTotalCount(profession);
            }
            if (!gender.equalsIgnoreCase("unknown")) {
                addToTotalCount(gender);
            }
            addToTotalCount(pregnant);
        } else {//judges and audits non-pregnant adult females
            if (needToPrint) { //indicates if it is a bot audit or user audit
                System.out.printf("- %s %s %s %s%n", bodyType, ageCategory, profession, gender);
            }
            if (!bodyType.equalsIgnoreCase("unspecified")) {
                addToTotalCount(bodyType);
            }
            addToTotalCount("human");
            addToTotalCount(ageCategory);
            if (!profession.equalsIgnoreCase("none")) {
                addToTotalCount(profession);
            }
            if (!gender.equalsIgnoreCase("unknown")) {
                addToTotalCount(gender);
            }
        }
    }
}
