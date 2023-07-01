import java.lang.Math;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Random;
import java.io.PrintWriter;
import java.io.FileWriter;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Acts as program entry
 * Contains algorithm for performing a bot judgement
 */

public class RescueBot {

    //Constructors

    /**
     * Default Constructor
     */

    public RescueBot() {

    }

    //Public Methods

    /**
     * Decides what location to save in each scenario
     * @param scenario     The scenario containing a choice of locations
     * @return Returns a location to save
     */

    final public Location decide(Scenario scenario) { //changed parameters to a final public method called from the RescueBot class to perform decisions in other classes
        ArrayList < Integer > whichLocation = new ArrayList < > ();
        // Characteristics taken into account: Number of entities in one location, pet or not, special speicies Ferret, if human or not, if pregnant or not, if baby or not, if profession is known or not
        for (int i = 0; i < scenario.getLocationArray().size(); i++) {
            for (int j = 0; j < scenario.getLocationArray().get(i).getEntityArray().size(); j++) {
                whichLocation.add(i); //add for how many entities are in the location
                Entity tempEntity = scenario.getLocationArray().get(i).getEntityArray().get(j);
                if (tempEntity instanceof Animal) {
                    Animal tempAnimal = (Animal) tempEntity;
                    if (tempAnimal.getPetStatus()) {
                        whichLocation.add(i); //if pet, add chances
                    }
                    if (tempAnimal.getSpecies().equalsIgnoreCase("ferret")) {
                        whichLocation.add(i); //if ferret, add chances
                    }
                }
                if (tempEntity instanceof Human) {
                    Human tempHuman = (Human) tempEntity;
                    whichLocation.add(i); //if human, add chances
                    if (tempHuman instanceof AdultFemale) {
                        AdultFemale tempAdultFemale = (AdultFemale) tempEntity;
                        if (tempAdultFemale.getPregnant()) {
                            whichLocation.add(i); //if pregnant, add chances
                        }
                    }
                    if (tempHuman.getAgeGroup().equalsIgnoreCase("baby")) {
                        whichLocation.add(i); //if baby,add chances
                    }
                    if (!(tempHuman.getProfession().equalsIgnoreCase("none"))) {
                        whichLocation.add(i); //if we know their profession, add chances
                    }
                }
            }
        }
        Random random = new Random();
        int randomLocation = random.nextInt(whichLocation.size());
        int returnLocation = whichLocation.get(randomLocation);
        return scenario.getLocationArray().get(returnLocation);
    }

    /**
     * Program Entry
     * @param args     Arguments provided by the user
     */

    public static void main(String[] args) {
        Randomizer randomizer = new Randomizer();
        String asciiFilePath = "welcome.ascii";
        File asciiFile = new File(asciiFilePath);
        Menus menus = new Menus();
        ValidateFile validateFile = new ValidateFile();
        RescueList rescueList = new RescueList();
        File scenarioFile = null;
        File logFile = null;
        boolean containsScenario = false;
        int scenarioCount = 0;
        int logCount = 0;
        boolean containsHelp = false;
        boolean containsLog = false;
        boolean logFileProvided = false;
        boolean logFileExists = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().equals("-h") || args[i].toLowerCase().equals("--help")) { //checks if -h or --help is called
                menus.helpMenu();
                containsHelp = true;
            }
            if (args[i].toLowerCase().equals("-s") || args[i].toLowerCase().equals("--scenarios")) { //checks if scenario provided
                containsScenario = true;
                if ((scenarioCount += 1) > 1) { //only 1 scenario file should be provided, otherwise a help menu is given and program exits
                    menus.helpMenu();
                }
                scenarioCount += 1;
            }
            if (args[i].toLowerCase().equals("-l") || args[i].toLowerCase().equals("--log")) { //checks if log file is provided
                if ((logCount += 1) > 1) { //only 1 log file should be provided, otherwise a help menu is given and program exits
                    menus.helpMenu();
                }
                logCount += 1;
                logFileProvided = true;
                containsLog = true;
            }
        }
        if (args.length > 0 && (!(containsLog || containsHelp || containsScenario))) { //checks if an argument aside from valid arguments are given
            menus.helpMenu(); //exits if it does and shows help menu
        }
        for (int i = 0; i < args.length; i++) { //starts reading and checking validity of arguments provided
            if (containsScenario) {
                if (args[i].toLowerCase().equals("-s") || args[i].toLowerCase().equals("--scenarios")) {
                    String scenarioFileName = "";
                    try {
                        scenarioFileName = args[i + 1];
                    } catch (IndexOutOfBoundsException e) { //catches out of bounds exception if a command is given incorrectly
                        menus.helpMenu();
                    }
                    scenarioFile = new File(scenarioFileName);
                    validateFile.checkFileCsv(scenarioFile);
                    try {
                        Scanner scanner = new Scanner(asciiFile);
                        while (scanner.hasNextLine()) {
                            System.out.println(scanner.nextLine());
                        }
                        scanner.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("asciiFile not found");
                    }
                    rescueList.readFile(scenarioFile);
                    System.out.printf("%d scenarios imported.\n", rescueList.getScenarioList().size());
                }
            }
            if (args[i].toLowerCase().equals("-l") || args[i].toLowerCase().equals("--log")) {
                String logFileName = "";
                try {
                    logFileName = args[i + 1];
                } catch (IndexOutOfBoundsException e) { //catches out of bounds exception if a command is given incorrectly
                    menus.helpMenu();
                }

                logFile = new File(logFileName); //gives log file to be used
                logFileProvided = true;
                if (logFile.exists()) {
                    logFileExists = true;
                }
            }
        }

        if (!containsScenario) { //randomizes a scenario if no scenario is provided
            try {
                Scanner scanner = new Scanner(asciiFile);
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("asciiFile not found");
            }
            rescueList.randomizedScenarios(randomizer.scenarioGenerator(0));
        }
        Scanner scanner = new Scanner(System.in); //initializes a scanner to be used for the program
        while (true) {
            String userInput = menus.mainMenu(scanner);
            if (!logFileProvided) { //performs checks on whether a rescuebot needs to be used, and if it needs to be but doesnt exist, create one
                if (logFile == null) {
                    if (new File("rescuebot.log").exists()) {
                        logFile = new File("rescuebot.log");
                    }
                    if (!new File("rescuebot.log").exists()) {
                        try {
                            if (new File("rescuebot.log").createNewFile()) {
                                logFile = new File("rescuebot.log");
                            }
                            PrintWriter printWriter = new PrintWriter(new FileWriter(logFile.getPath(), false));
                            printWriter.printf("LOGFILE FOR RESCUEBOT%n%n"); //initial title for a newly created rescuebot log
                            printWriter.close();
                        } catch (IOException e) {
                            System.out.println("Unable to create rescuebot.log"); //thrown when unable to create file due to permissions
                        }
                    }
                }
            }
            menus.menuCommandsExecute(userInput, rescueList.getScenarioList(), logFile, scanner, containsScenario, logFileProvided, logFileExists, rescueList); //enters the main menu
        }

    }
}
