import java.util.ArrayList;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Class containing instance variables connected to locations
 * Class of locations to select
 */

public class Location {

    //Instance Variables

    private boolean trespassing;
    private String latitude;
    private String longitude;
    private ArrayList < Entity > entities;
    private String trespassingString;

    //Constructors

    /**
     * Default constructor for Locations
     */

    public Location(){

    }

    /**
     * Constructor for location initializing instance variables with given parameters
     * @param   trespassing     Indicates whether entities in this location are trespassing, changed to a String and stored
     * @param   latitude        The latitude of the location
     * @param   longitude       The logitude of the location
     */

    public Location(boolean trespassing, String latitude, String longitude) {
        this.entities = new ArrayList < Entity > ();
        this.trespassing = trespassing;
        this.latitude = latitude;
        this.longitude = longitude;
        if (trespassing) { //sets trespassing string depending on boolean value
            this.trespassingString = "yes";
        } else {
            this.trespassingString = "no";
        }
    }

    //Public Methods

    //Accessors

    /**
     * Gets the ArrayList of entities 
     * @return   Returns an ArrayList of entities, containg all the entities in a location
     */

    public ArrayList < Entity > getEntityArray() {
        return entities;
    }

    /**
     * Gets the trespassing status of entities in the location
     * @return   Returns a boolean indicating if entities are trespassing
     */

    public boolean returnTrespassing() {
        return this.trespassing;
    }

    /**
     * Gets the latitude of a location
     * @return   Returns a String of the latitude of current location
     */

    public String getLocationLatitude() {
        return this.latitude;
    }

    /**
     * Gets the longitude of a location
     * @return   Returns a String of the longitude of current location
     */

    public String getLocationLongitude() {
        return this.longitude;
    }

    /**
     * Gets the trespassing status of a location as a String, either "legal" or "trespassing" will be returned
     * @return   Returns a String of the trespassing status of the location
     */

    public String getTrespassingString() {
        return trespassingString;
    }

    //Mutators

    /**
     * Adds an Entity to the location
     * @param   entity  Entity to be added
     */

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
}
