import java.io.File;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Comparator;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Class with methods to make, save, and print statistics
 * Contains method to check if a log file is provided but does not exist
 */

public class Audit {

    //Instance Variables

    private ArrayList < String[] > auditArrayList;
    private double entityCount;
    private double totalAge;

    //Constructors

    /**
     * Default constructor for Audit
     * Initializes instance variables
     */

    public Audit() {
        auditArrayList = new ArrayList < String[] > ();
        entityCount = 0;
        totalAge = 0;
    }

    //Public Methods

    //Mutators

    /**
     * Reads in and creates a statistic when a location is provided
     * @param   location       Location to add to statistic
     * @param   audit          Passes in same audit to keep statistic culmulative
     * @param   judge          Passes in judge to call methods to add to the total count statistic in Class judge
     */

    public void makeStatistic(Location location, Audit audit, Judge judge) {
        for (int i = 0; i < location.getEntityArray().size(); i++) {
            if (location.returnTrespassing()) {
                judge.addChosenCount("trespassing");
            } else {
                judge.addChosenCount("legal");
            }
            Entity currentEntity = location.getEntityArray().get(i);
            if (currentEntity instanceof AdultFemale) { //performs checks for adult females
                entityCount += 1; //gets total count of humans in a location that was chosen
                totalAge += currentEntity.getAge(); //gets total age of humans in a location that was chosen
                AdultFemale currentCheckAdultFemale = (AdultFemale) currentEntity;
                String bodyType = currentCheckAdultFemale.getBodyType();
                String ageCategory = currentCheckAdultFemale.getAgeGroup();
                String gender = currentCheckAdultFemale.getGender();
                String profession = currentCheckAdultFemale.getProfession();
                String pregnant = null;
                if (currentCheckAdultFemale.getPregnant()) { //performs checks for pregnant adult females
                    pregnant = "pregnant";
                    if (!bodyType.equalsIgnoreCase("unspecified")) {
                        judge.addChosenCount(bodyType);
                    }
                    judge.addChosenCount("human");
                    judge.addChosenCount(ageCategory);
                    if (!profession.equalsIgnoreCase("none")) {
                        judge.addChosenCount(profession);
                    }
                    if (!gender.equalsIgnoreCase("unknown")) {
                        judge.addChosenCount(gender);
                    }
                    judge.addChosenCount(pregnant);
                } else { //performs checks for not pregnant adult females
                    if (!bodyType.equalsIgnoreCase("unspecified")) { //adds to statistic only if body type is accepted and not unspecified
                        judge.addChosenCount(bodyType);
                    }
                    judge.addChosenCount(ageCategory);
                    judge.addChosenCount("human");
                    if (!profession.equalsIgnoreCase("none")) {//adds to statistic only if profession is accepted and not none
                        judge.addChosenCount(profession);
                    }
                    if (!gender.equalsIgnoreCase("unknown")) {//adds to statistic only if gender is accepted and not unknown
                        judge.addChosenCount(gender);
                    }
                }
            } else if (currentEntity instanceof Human) { //performs checks for humans that are not adult females
                entityCount += 1;//gets total count of humans in a location that was chosen
                totalAge += currentEntity.getAge();//gets total age of humans in a location that was chosen
                Human currentCheckHuman = (Human) currentEntity;
                String bodyType = currentCheckHuman.getBodyType();
                String ageCategory = currentCheckHuman.getAgeGroup();
                String gender = currentCheckHuman.getGender();
                if (ageCategory.equalsIgnoreCase("adult")) {
                    String profession = currentCheckHuman.getProfession();
                    if (!bodyType.equalsIgnoreCase("unspecified")) {//adds to statistic only if body type is accepted and not unspecified
                        judge.addChosenCount(bodyType);
                    }
                    judge.addChosenCount("human");
                    judge.addChosenCount(ageCategory);
                    if (!profession.equalsIgnoreCase("none")) {//adds to statistic only if profession is accepted and not none
                        judge.addChosenCount(profession);
                    }
                    if (!gender.equalsIgnoreCase("unknown")) {//adds to statistic only if gender is accepted and not unknown
                        judge.addChosenCount(gender);
                    }
                } else if (!ageCategory.equalsIgnoreCase("adult")) { //performs checks for non-adults
                    if (!bodyType.equalsIgnoreCase("unspecified")) {
                        judge.addChosenCount(bodyType);
                    }
                    judge.addChosenCount(ageCategory);
                    judge.addChosenCount("human");
                    if (!gender.equalsIgnoreCase("unknown")) {
                        judge.addChosenCount(gender);
                    }
                }
            } else if (currentEntity instanceof Animal) { //performs checks for animals
                Animal currentCheckAnimal = (Animal) currentEntity;
                boolean doNotIncludeSpecies = false;
                String species = currentCheckAnimal.getSpecies();
                if (species.equalsIgnoreCase("unknown")) { //only added if species is valid and not unknown
                    doNotIncludeSpecies = true;
                }
                if (currentCheckAnimal.getPetStatus()) {
                    judge.addChosenCount("pet");
                    judge.addChosenCount("animal");
                    if (!doNotIncludeSpecies) {
                        judge.addChosenCount(species);
                    }
                } else {
                    if (!doNotIncludeSpecies) {
                        judge.addChosenCount(species);
                    }
                    judge.addChosenCount("animal");
                }
            }
        }
    }

    //Other public methods

    /**
     * Checks if a log file is provided and exists, printing an error and exiting if given log does not exist
     * @param   logFileProvided     True if user provides a log file
     * @param   logFileExists       True if the log file exists
     */

    public void checkIfLogExists(boolean logFileProvided, boolean logFileExists) { 
        if (logFileProvided && !logFileExists) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");
            System.exit(0);
        }
    }

    /**
     * Saves audit to log file
     * @param   logFile                             Log file to be read to
     * @param   totalCountStatistic                 Statistics to be added to log file
     * @param   fromWho                             Indicates whether saving from a bot or user input
     * @param   logFileProvided                     True if user provides a log file, used to check if log file exists
     * @param   logFileExists                       True if the log file exists, used to check if log file exists
     * @param   totalScenarios                      Total number of scenarios run to obtain the statistic
     * @param   totalAge                            Total age of humans in the statistic
     * @param   totalEntities                       Total number of humans in the statistic
     */

    public void saveAudit(File logFile, ArrayList < String > totalCountStatistic, String fromWho, boolean logFileProvided, boolean logFileExists, int totalScenarios, int totalAge, int totalEntities) {
        checkIfLogExists(logFileProvided, logFileExists);
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(logFile.getPath(), true));
            printWriter.print("YYYY-MM-DD DATE//" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "//HH-MM-SS TIME//" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));
            if (fromWho.equalsIgnoreCase("userRun")) { //saving user audits
                printWriter.print("//USERAUDIT");
            } else if (fromWho.equalsIgnoreCase("robotRun")) { //saving bot audits
                printWriter.print("//BOTAUDIT");
            }
            printWriter.print("//TOTAL-SCENARIOS//" + String.valueOf(totalScenarios)); //saving scenarios run in an audit
            printWriter.println("//TOTAL-HUMAN-AGE//" + String.valueOf(totalAge) + "//TOTAL-HUMANS//" + String.valueOf(totalEntities) + "//"); //saving total age and number of humans for auditing
            int shiftCount = 3;
            int totalShiftCount = 2;
            for (int i = 0; i < totalCountStatistic.size(); i += shiftCount) {
                printWriter.println(totalCountStatistic.get(i) + ":SAVED:" + totalCountStatistic.get(i + 1) + ":TOTAL:" + totalCountStatistic.get(i + totalShiftCount)); //saving statistics created during judging
            }
            printWriter.println("");
            printWriter.close();
        } catch (Exception e) {
            System.out.print("Error in writing to logFile");
        }
    }

    /**
     * Prints a statistic
     * @param   totalCountStatistic        Statistic to be read from
     * @param   scenarioNumber             Indicates number of scenarios judged
     * @param   auditOrNot                 Checks if being called from an audit
     */

    public void printAudit(ArrayList < String > totalCountStatistic, int scenarioNumber, boolean auditOrNot) {
        for (int i = 0; i < totalCountStatistic.size(); i += 3) {
            String currentCharacteristic = totalCountStatistic.get(i);
            double numerator = Double.parseDouble(totalCountStatistic.get(i + 1));
            int denominatorPosition = 2;
            double denominator = Double.parseDouble(totalCountStatistic.get(i + denominatorPosition));
            double ratio = Math.ceil((numerator / denominator) * 100) / 100; //rounds statistics up
            boolean isNotIn = true; //check if value is in the statistic to be printed
            for (int j = 0; j < auditArrayList.size(); j++) {
                if (auditArrayList.get(j)[0].equalsIgnoreCase(currentCharacteristic)) {
                    isNotIn = false;
                }
            }
            if (isNotIn) { //if not in yet, adds its survival ratio to be printed
                String[] insert = {
                    currentCharacteristic,
                    String.valueOf(ratio)
                };
                auditArrayList.add(insert);
            }
        }
        ArrayList < String[] > arraySorted = auditArrayList;
        arraySorted.sort(Comparator.comparing((String[] array) -> Double.parseDouble(array[1])).reversed().thenComparing(array -> array[0])); //sorts the list, from descending ratio then alphabetically
        if (!auditOrNot) {
            System.out.println("======================================");
            System.out.println("# Statistic");
            System.out.println("======================================");
            System.out.printf("- %% SAVED AFTER %d RUNS%n", scenarioNumber + 1);
        }
        for (int j = 0; j < arraySorted.size(); j++) {
            System.out.printf("%s: %.2f%n", arraySorted.get(j)[0], Double.parseDouble(arraySorted.get(j)[1]));
        }
        if (!auditOrNot) {
            double avgAge = Math.ceil((totalAge / entityCount) * 100) / 100; //rounds the average age up
            System.out.println("--");
            System.out.printf("average age: %.2f%n", avgAge); //shows age to 2 decimal places
        }
    }
}
