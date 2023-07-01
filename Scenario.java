import java.util.ArrayList;
import CustomExceptions.*;
import Entities.*;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Jule Valendo Halim 
 * student id: 1425567
 * student email: julevalendoh@student.unimelb.edu.au
 *
 * Class containing instance variables regarding scenarios
 * Class of scenarios to judge
 */

public class Scenario {

    //Instance Variables

    private String scenarioName;
    private ArrayList < Location > locationArray;

    //Constructor

    /**
     * Default constructor for Scenarios, initializes locationArray
     */

    public Scenario(){
        locationArray=new ArrayList < Location > ();
    }

    /**
     * Constructor with scenario name provided and initializes instance variables
     * @param   scenarioName    Name of scenario for scenarioName
     */

    public Scenario(String scenarioName) {
        this.scenarioName = scenarioName;
        locationArray = new ArrayList < Location > ();
    }

    //Public Methods

    //Accessors

    /**
     * Gets the arrayList of locations
     * @return   Returns an ArrayList of locations, containg all the locations in a scenario
     */

    public ArrayList < Location > getLocationArray() {
        return locationArray;
    }

    /**
     * Gets the location at a specified position
     * @param   locationIndex   Index of location to be obtained
     * @return  Returns a location at specified index
     */

    public Location getLocation(int locationIndex) {
        return locationArray.get(locationIndex);
    }

    /**
     * Gets the name of the scenario
     * @return  Returns a String of the scenario name
     */

    public String getScenarioName() {
        return scenarioName;
    }

    //Mutators

    /**
     * Adds a location to the scenario
     * @param    location   Location to be added to the scenario
     */

    public void addLocation(Location location) {
        locationArray.add(location);
    }
}
