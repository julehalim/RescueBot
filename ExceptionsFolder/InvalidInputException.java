package CustomExceptions;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * An exception for invalid inputs
 */

public class InvalidInputException extends RuntimeException{

    //Constructors

    /**
     * Default constructor for InvalidInputException
     */

    public InvalidInputException(){

    }

    /**
     * Constructor for InvalidInputException with a message provided
     * @param   message     Passes a message, calling to the super of parent RuntimeException
     */

    public InvalidInputException(String message){
        super(message);
    }
}
