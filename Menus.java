import java.lang.Math;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.ArrayList;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 * 
 * Contains the menus needed and executes user inputs based on replies to main menu
 */

public class Menus {

    private Audit audit;
    private enum possibleInputs {JUDGE,J,RUN,R,AUDIT,A,QUIT,Q};

    //Constructors

    /**
     * Default constructor for Menus, initializes an Audit
     */

    public Menus() {
        audit = new Audit();
    }

    //Public Methods

    /**
     * Prints the help menu, terminating the program
     * Executed under conditions in which incorrect arguments are provided or a scenario file is not found
     */

    public void helpMenu() { //shows help menu
        System.out.println("RescueBot - COMP90041 - Final Project");
        System.out.println("");
        System.out.println("Usage: java RescueBot [arguments]");
        System.out.println("");
        System.out.println("Arguments:");
        System.out.printf("-s or --scenarios\tOptional: path to scenario file\n");
        System.out.printf("-h or --help\t\tOptional: Print Help (this message) and exit\n");
        System.out.printf("-l or --log\t\tOptional: path to data log file\n");
        System.exit(0);
    }

    /**
     * Returns a string of user input on main menu
     * @param       scanner     Scanner used to get user input
     * @return      Returns a string of user input if valid
     */

    public String mainMenu(Scanner scanner) {
        String inputString;
        int errorOrNot = 0;
        while (true) { //loops until valid user input provided
            if (errorOrNot == 0) {
                System.out.println("Please enter one of the following commands to continue:");
            }
            System.out.println("- judge scenarios: [judge] or [j]");
            System.out.println("- run simulations with the in-built decision algorithm: [run] or [r]");
            System.out.println("- show audit from history: [audit] or [a]");
            System.out.println("- quit the program: [quit] or [q]");
            System.out.print("> ");
            try {
                inputString = scanner.nextLine();
                boolean correctInput = false;
                for (possibleInputs i: possibleInputs.values()) {
                    if (i.name().equalsIgnoreCase(inputString)) {
                        correctInput = true;
                    }
                }
                if (inputString.isEmpty()) {
                    throw new InputMismatchException();
                }
                if (correctInput) {
                    break;
                }
                errorOrNot = 1;
                System.out.println("Invalid command! Please enter one of the following commands to continue:");
            } catch (InputMismatchException e) {
                System.out.println("Invalid command! Please enter one of the following commands to continue:");
                continue;
            }

        }
        return inputString;
    }

    /**
     * Executes the command from the user input obtained from main menu
     * @param   userInput           User input to execute command as given by user
     * @param   scenarioList        List of scenarios to execute commands on
     * @param   logFile             File to be written to if user consented or a bot run is performed
     * @param   scanner             Used to get user input
     * @param   containsScenario    Indcates whether a scenario is provided
     * @param   logFileProvided     Indicates whether a log file is provided
     * @param   logFileExists       Indicates if a log file exists if provided
     * @param   rescueList          Passes RescueList object to perform certain methods
     * @throws  InvalidInputException       Thrown when user inputs an incorrect data type
     * @throws  InputMismatchException      Occurs when parsing produces an error
     */

    public void menuCommandsExecute(String userInput, ArrayList < Scenario > scenarioList, File logFile, Scanner scanner, boolean containsScenario, boolean logFileProvided, boolean logFileExists, RescueList rescueList) {
        Judge judge = new Judge();
        ArrayList < Scenario > tempJudgeScenarioList=scenarioList;
        Randomizer randomizer = new Randomizer();
        if(!containsScenario){
            RescueList randomRescueList = new RescueList();
            randomRescueList.randomizedScenarios(randomizer.scenarioGenerator(0));
            tempJudgeScenarioList = randomRescueList.getScenarioList();
        } else if(containsScenario){
            tempJudgeScenarioList = scenarioList;
        }
        String userConsent = null;
        int errorOrNot = 0;
        if (userInput.equalsIgnoreCase("judge") || userInput.equalsIgnoreCase("j")) {
            boolean yesOrNoProvided = false;
            while (!yesOrNoProvided) { //loops until user consents or doesn't consent
                try {
                    if (errorOrNot == 0) {
                        System.out.println("Do you consent to have your decisions saved to a file? (yes/no)"); //checks for user consent to save file
                        System.out.print("> ");
                    }
                    userConsent = scanner.next();
                    if (userConsent.equalsIgnoreCase("yes") || userConsent.equalsIgnoreCase("no")) {
                        yesOrNoProvided = true;
                    } else {
                        throw new InvalidInputException("Invalid response! Do you consent to have your decisions saved to a file? (yes/no)");
                    }
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                    System.out.print("> ");
                    errorOrNot = 1;
                }
            }
            judge.judgeScenario(userConsent, tempJudgeScenarioList, logFile, scanner, judge, logFileProvided, logFileExists, containsScenario); //lets user judge
        }

        if (userInput.equalsIgnoreCase("run") || userInput.equalsIgnoreCase("r")) { //performs a bot algorithm
            ArrayList < Scenario > tempScenarioList = scenarioList;
            int howMany=0; //checks how many scenarios run, executes only if no scenario is provided
            if (containsScenario) {
                tempScenarioList = scenarioList;
            }
            if (!containsScenario) { //executes only if no scenarios provided
                while (true) {
                    try {
                        System.out.println("How many scenarios should be run?");
                        System.out.print("> ");
                        howMany = Integer.parseInt(scanner.nextLine());
                        if (howMany < 1) {
                            throw new InputMismatchException();
                        }
                        break;
                    } catch (InputMismatchException e) { //executes if wrong data type passed
                        System.out.print("Invalid input! ");
                    } catch (NumberFormatException e){ //executes if parsing into int fails
                        System.out.print("Invalid input! ");
                    }
                }
                RescueList tempRescueList = new RescueList();
                Randomizer tempRandomizer = new Randomizer();
                tempRescueList.randomizedScenarios(tempRandomizer.scenarioGenerator(howMany)); //creates a randomized scenario to judge
                tempScenarioList = tempRescueList.getScenarioList();
            }
            judge.botJudge(tempScenarioList, logFile, judge, logFileProvided, logFileExists);
            thatsAll(scanner, false); //shows message, asking user for input of enter
        }

        if (userInput.equalsIgnoreCase("audit") || userInput.equalsIgnoreCase("a")) {
            performAudit(logFile,logFileProvided,logFileExists,audit,scanner);
        }
        if (userInput.equalsIgnoreCase("quit") || userInput.equalsIgnoreCase("q")) {
            System.exit(0);
        }
    }

    /**
     * Prints the message asking the user to press enter to return to main menu
     * Executes when certain actions have finished being performed, or there is no history to audit from a log file
     * @param   scanner         Scanner used to get user input
     * @param   hasNoHistory    Indicates if method is executed due to no history
     */

    public void thatsAll(Scanner scanner, boolean hasNoHistory) {
        while (true) {
            try {
                if (!hasNoHistory) { //if method called from not having any history
                    System.out.println("That's all. Press Enter to return to main menu.");
                } else if (hasNoHistory) { //method called from any other condition
                    System.out.println("No history found. Press enter to return to main menu.");
                }
                System.out.print("> ");
                scanner.nextLine();
                break;
            } catch (Exception e) {
                System.out.print("Invalid input! ");
                continue;
            }
        }
    }

    //Private Methods

    /**
     * Performs an audit on a log file, printing out the audit if any exists
     * @param   logFile             File to read audits from
     * @param   logFileProvided     Indicates if user provided a log file
     * @param   logFileExists       Indicates if user-provided log file
     * @param   scanner             Used to get user input
     * @throws  Exception           Thrown when an exception occur in the logFIle, which should never happen
     */

    private void performAudit(File logFile, boolean logFileProvided, boolean logFileExists, Audit audit, Scanner scanner) {
        audit.checkIfLogExists(logFileProvided, logFileExists); //check if logFile provided by user but doesn't exist
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(logFile);
        } catch (FileNotFoundException e) {
            System.out.println("error in scanning logFile");
        } catch (Exception e) {
            System.out.println("some other error");
        }
        boolean userRead = false;
        ArrayList < String > botAuditList = new ArrayList < String > ();
        ArrayList < String > userAuditList = new ArrayList < String > ();
        int userHowManyRuns = 0;
        int botHowManyRuns = 0;
        int totalBotScenarios = 0;
        int totalBotAge = 0;
        int totalBotEntities = 0;
        int totalUserScenarios = 0;
        int totalUserAge = 0;
        int totalUserEntities = 0;
        while (fileScanner.hasNextLine()) { //iterating through every line
            try {
                String currentRead = fileScanner.nextLine();
                String[] currentReadSplit = currentRead.split(":", -1);
                ArrayList < String > currentReadingList = new ArrayList < String > ();
                for (String field: currentReadSplit) {
                    if (field.toLowerCase().contains("useraudit".toLowerCase())) {
                        userRead = true; //means currently read log is an user log
                        userHowManyRuns += 1;
                    } else if (field.toLowerCase().contains("botaudit".toLowerCase())) {
                        userRead = false; //means currently read log is a bot log
                        botHowManyRuns += 1;
                    }
                    if (field.toLowerCase().contains("age//".toLowerCase())) {
                        int startAge = field.indexOf("AGE//") + "AGE//".length(); //gets total age of humans for an audit
                        int endAge = field.indexOf("//", startAge);
                        int startEntity = field.indexOf("TOTAL-HUMANS//") + "TOTAL-HUMANS//".length(); //gets total number of humans for an audit
                        int endEntity = field.indexOf("//", startEntity);
                        int startScenario = field.indexOf("SCENARIOS//") + "SCENARIOS//".length(); //gets number of scenarios run in an audit
                        int endScenario = field.indexOf("//", startScenario);
                        if (userRead) { //adds to user audit if comes from user log
                            totalUserAge += Integer.parseInt(field.substring(startAge, endAge));
                            totalUserEntities += Integer.parseInt(field.substring(startEntity, endEntity));
                            totalUserScenarios += Integer.parseInt(field.substring(startScenario, endScenario));
                        }
                        if (!userRead) { //adds to bot audit if comes from bot log
                            totalBotAge += Integer.parseInt(field.substring(startAge, endAge));
                            totalBotEntities += Integer.parseInt(field.substring(startEntity, endEntity));
                            totalBotScenarios += Integer.parseInt(field.substring(startScenario, endScenario));
                        }
                    }
                }
                int logLength=5;
                if (currentReadSplit.length == logLength) { //length of a row that contains characteristic information
                    if (userRead) { //accesses which list depending on whose log it is
                        currentReadingList = userAuditList;
                    } else if (!userRead) { //accesses which list depending on whose log it is
                        currentReadingList = botAuditList;
                    }
                    boolean inListOrNot = false;
                    //indexes of where information is from the log file
                    int indexOfProperty = 0;
                    int numeratorShift = 2;
                    int denominatorShift = 4;
                    int denominatorInArray = 2;
                    for (int i = 0; i < currentReadingList.size(); i++) { //checks if a characteristic has already been added to log
                        if (currentReadSplit[0].equalsIgnoreCase(currentReadingList.get(i))) {
                            inListOrNot = true;
                            indexOfProperty = i;
                            break;
                        }
                    }
                    if (!inListOrNot) { //executes if characteristic was not read before
                        if (userRead) { //runs depending on user or bot log, updating values as necessary
                            userAuditList.add(currentReadSplit[0]);
                            userAuditList.add(currentReadSplit[numeratorShift]);
                            userAuditList.add(currentReadSplit[denominatorShift]);
                        }
                        if (!userRead) { //runs depending on user or bot log, updating values as necessary
                            botAuditList.add(currentReadSplit[0]);
                            botAuditList.add(currentReadSplit[numeratorShift]);
                            botAuditList.add(currentReadSplit[denominatorShift]);
                        }
                    } else if (inListOrNot) { //executes if characteristic was read before
                        if (userRead) { //runs depending on user or bot log, updating values as necessary
                            int numerator = Integer.parseInt(userAuditList.get(indexOfProperty + 1));
                            int denominator = Integer.parseInt(userAuditList.get(indexOfProperty + denominatorInArray));
                            int addNumerator = Integer.parseInt(currentReadSplit[numeratorShift]);
                            int addDenominator = Integer.parseInt(currentReadSplit[denominatorShift]);
                            int newNumerator = numerator + addNumerator;
                            int newDenominator = denominator + addDenominator;
                            userAuditList.set(indexOfProperty + 1, String.valueOf(newNumerator));
                            userAuditList.set(indexOfProperty + denominatorInArray, String.valueOf(newDenominator));
                        } else if (!userRead) { //runs depending on user or bot log, updating values as necessary
                            int numerator = Integer.parseInt(botAuditList.get(indexOfProperty + 1));
                            int denominator = Integer.parseInt(botAuditList.get(indexOfProperty + denominatorInArray));
                            int addNumerator = Integer.parseInt(currentReadSplit[numeratorShift]);
                            int addDenominator = Integer.parseInt(currentReadSplit[denominatorShift]);
                            int newNumerator = numerator + addNumerator;
                            int newDenominator = denominator + addDenominator;
                            botAuditList.set(indexOfProperty + 1, String.valueOf(newNumerator));
                            botAuditList.set(indexOfProperty + denominatorInArray, String.valueOf(newDenominator));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("an exception occured in reading logFile"); //exception handling if there is an error in reading log file that is not caused by non-existent log
                continue;
            }
        }
        fileScanner.close();
        if (userAuditList.isEmpty() && botAuditList.isEmpty()) {
            thatsAll(scanner, true);
        }
        if (!(botAuditList.isEmpty())) {
            printBotAudit(totalBotScenarios, botHowManyRuns, totalBotAge, totalBotEntities, botAuditList, audit, userAuditList);
        }
        if (!(userAuditList.isEmpty())) {
            printUserAudit(totalUserScenarios, userHowManyRuns, totalUserAge, totalUserEntities, userAuditList, audit);
        }
        if (!(userAuditList.isEmpty() && botAuditList.isEmpty())) {
            thatsAll(scanner, false);
        }
    }

    /**
     * Prints the user audit if a user audit exists
     * @param   totalUserScenarios  Total number of scenarios in user audits
     * @param   totalUserAge        Total age of humans surviving in all user audits saved
     * @param   totalUserEntities   Total number of humans surviving in all user audits saved
     * @param   userAuditList       List of characteristics saved, including surivial rates for each characteristics
     * @param   audit               Audit used to execute methods related to printing statistics
     */

    private void printUserAudit(int totalUserScenarios, int userHowManyRuns, int totalUserAge, int totalUserEntities, ArrayList < String > userAuditList, Audit audit){
        System.out.println("======================================");
        System.out.println("# User Audit");
        System.out.println("======================================");
        System.out.printf("- %% SAVED AFTER %d RUNS%n", totalUserScenarios);
        audit.printAudit(userAuditList, userHowManyRuns, true);
        double totalUserAgeDouble = totalUserAge;
        double totalUserEntitiesDouble = totalUserEntities;
        double avgAge = Math.ceil((totalUserAgeDouble / totalUserEntitiesDouble) * 100) / 100.0; //rounds the age up
        System.out.println("--");
        System.out.printf("average age: %.2f%n", avgAge);
    }

    /**
     * Prints the bot audit if a bot audit exists
     * @param   totalBotScenarios       Total number of scenarios in bot audits
     * @param   totalBotAge             Total age of humans surviving in all bot audits saved
     * @param   totalBotEntities        Total number of humans surviving in all bot audits saved
     * @param   botAuditList            List of characteristics saved, including surivial rates for each characteristics
     * @param   audit                   Audit used to execute methods related to printing statistics
     * @param   userAuditList           Used to check if user audit is empty, and whether to print a new line
     */

    private void printBotAudit(int totalBotScenarios, int botHowManyRuns, int totalBotAge, int totalBotEntities, ArrayList < String > botAuditList, Audit audit, ArrayList < String > userAuditList){
        System.out.println("======================================");
        System.out.println("# Algorithm Audit");
        System.out.println("======================================");
        System.out.printf("- %% SAVED AFTER %d RUNS%n", totalBotScenarios);
        double totalBotAgeDouble = totalBotAge;
        double totalBotEntitiesDouble = totalBotEntities;
        audit.printAudit(botAuditList, botHowManyRuns, true);
        double avgAge = Math.ceil((totalBotAgeDouble / totalBotEntitiesDouble) * 100) / 100.0; //rounds the age up
        System.out.println("--");
        System.out.printf("average age: %.2f%n", avgAge);
        if (!(userAuditList.isEmpty())) { //prints a space only if an algorithm audit will also be printed
            System.out.println("");
        }
    }
}
