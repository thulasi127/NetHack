package rogue;

import java.util.ArrayList;

public class Door {
    private ArrayList<Room> connectedRooms = new ArrayList<>();;
    private String wallDir;
    private int wallPos;

    /**
     * Default constructor.
     */
    public Door() {
        this("N", 1);
    }

    /**
     * Constructor that reads in and sets the direction and position of the wall.
     * @param direction - either values N,S,E,W
     * @param position - position of wall
     */
    public Door(String direction, int position) {
        wallDir = direction;
        wallPos = position;
    }

    /**
     * Method that acts as an accessor to retrieve the wall position of the door.
     * @return wallPos
     */
    public int getWallPosition() {
        return wallPos;
    }

    /**
     * Method that acts as an accessor to retrieve the direction of the wall.
     * @return wallDir - either: "N", "S", "E", "W"
     */
    public String getDirection() {
        return wallDir;
    }

    /**
     * Specify one of the two rooms that can be attached to a door.
     * @param r - the room that will be attached
     */
    public void connectRoom(Room r) {
        connectedRooms.add(r);
    }

    /**
     * Get an Arraylist that contains both rooms connected by this door.
     * @return connectedRooms - Array list of rooms
     */
    public ArrayList<Room> getConnectedRooms() {
        return connectedRooms;
    }

    /**
     * Get the connected room by passing in the current room.
     * @param currentRoom
     * @return the room at a specific position compared the the current Room
     */
    public Room getOtherRoom(Room currentRoom) {
        if (connectedRooms.get(0) != currentRoom) {
            return connectedRooms.get(0);
        } else {
            return connectedRooms.get(1);
        }
    }
}
