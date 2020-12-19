package rogue;

import java.awt.Point;

/**
 * A basic function to represent the player character.
 */
public class Player {
    private Point xyLocation;
    private Room currentRoom;
    private String name;

    /**
     * Default constructor.
     */
    public Player() {
    }

    /**
     * Constructor which inititiates the player's name.
     * @param playerName - Player's new name
     */
    public Player(String playerName) {
        name = playerName;
        xyLocation = new Point(1, 1);
        currentRoom = null;
    }

    /**
     * Accessor method to retrieve player's name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Mutator method to set player's name.
     * @param playerName - represents the player's new name
     **/
    public void setName(String playerName) {
        name = playerName;
    }

    /**
     * Accessor method to retrieve player's location.
     * @return xyLocation
     */
    public Point getXyLocation() {
        return xyLocation;
    }

    /**
     * Mutator method to set player's position.
     * @param newXyLocation - new player's position
     */
    public void setXyLocation(Point newXyLocation) {
        xyLocation = newXyLocation;
    }

    /**
     * Accessor method to retrieve player's current room.
     * @return currentRoom
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

     /**
     * Mutator method to set player's new Room.
     * @param newRoom - the player's new room
     */
    public void setCurrentRoom(Room newRoom) {
        currentRoom = newRoom;
    }
}
