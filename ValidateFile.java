import java.lang.Math;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Validates files passed by user argument
 */
 
public class ValidateFile {

    //Constructors

    /**
     * Default constructor for file validation class
     */

    public ValidateFile() {

    }

    //Public Methods

    /**
     * Checks if csv file exists
     * @param   file      CSV file to be checked
     */

    public void checkFileCsv(File file) {
        Menus menus = new Menus();
        if (!file.exists()) { //checks if scenario file exists; if not, throw exception and show help menu
            try {
                throw new FileNotFoundException("could not find scenarios file.");
            } catch (FileNotFoundException e) {
                System.out.println("java.io.FileNotFoundException: " + e.getMessage());
                menus.helpMenu();
            }
        }
    }
}
