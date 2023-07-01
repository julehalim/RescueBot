package CustomExceptions;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * An exception for invalid characteristics
 */

public class InvalidCharacteristicException extends RuntimeException{

    //Constructors

    /**
     * Default constructor for InvalidCharacteristicExceptions
     */

    public InvalidCharacteristicException(){
        
    }

    /**
     * Constructor for InvalidCharacteristicExceptions with a message provided
     * @param   message     Passes a message, calling to the super of parent RuntimeException
     */

    public InvalidCharacteristicException(String message){
        super(message);
    }
}
