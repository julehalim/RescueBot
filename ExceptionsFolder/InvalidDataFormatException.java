package CustomExceptions;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * An exception for when an invalid data format is given
 */

public class InvalidDataFormatException extends RuntimeException{
    //Constructors

    /**
     * Default constructor for InvalidDataFormatException
     */

    public InvalidDataFormatException(){

    }

    /**
     * Constructor for InvalidDataFormatException with a message provided
     * @param   message     Passes a message, calling to the super of parent RuntimeException
     */

    public InvalidDataFormatException(String message){
        super(message);
    }
}
