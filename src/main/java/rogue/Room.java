package rogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;


/**
 * Class that represents a room within the dungeon.
 * Room contains items(treasures), players (monsters), doors, etc.
 */
public class Room {
    private int height;
    private int width;
    private int id;
    private boolean status;
    private Map<String, Door> doors;
    private ArrayList<Item> roomItems;
    private ArrayList<Door> doorList;
    private Player player;
    private Map<String, Character> symbolMap;

    /**
     * Default constructor.
     */
    public Room() {
        id = 0;
        height = 0;
        width = 0;
        status = false;
        player = null;
        doors = new HashMap<>();
        symbolMap = new HashMap<>();
        roomItems = new ArrayList<>();
        doorList = new ArrayList<>();

    }

    /**
     * Mutator method to sets the symbols in each room.
     * @param newSymbols - new set of symbols in room
     */
    public void setSymbols(Map<String, Character> newSymbols) {
        symbolMap = newSymbols;
    }

   /**
     * Mutator method to set the starting room.
     * @param start - boolean start value (true if the room is a starting room)
     */
    public void setIsStartingRoom(boolean start) {
        status = start;
    }

    /**
     * Accessor method to retrieve the starting room.
     * @return true - if the room is a starting room
     */
    public boolean getIsStartingRoom() {
        return status;
    }

    /**
     * Accessor method to retrieve the room's width.
     * @return width - room's current width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Mutator method to set the room's width.
     * @param newWidth - the new width of the room
     */
    public void setWidth(int newWidth) {
        width = newWidth;
    }

    /**
     * Accessor method to retrieve the room's height.
     * @return height - room's current height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Mutator method to set the room's height.
     * @param newHeight - room's new height
     */
    public void setHeight(int newHeight) {
        height = newHeight;
    }

    /**
     * Accessor method to retrieve the room's Id.
     * @return id - room's id value
     */
    public int getId() {
        return id;
    }

    /**
     * Mutator method to set the room's id.
     * @param newId - room's new id
     */
    public void setId(int newId) {
        id = newId;
    }

    /**
     * Accessor method to retrieve the list of items currently in the room.
     * @return roomItems - current list of items in the room
     */
    public ArrayList<Item> getRoomItems() {
        return roomItems;
    }

    /**
     * Mutator method to set the list of items currently in the room.
     * @param newRoomItems - new list of items in the room
     */
    public void setRoomItems(ArrayList<Item> newRoomItems) {
        roomItems = newRoomItems;
    }


    /**
     * Accessor method to retrieve the player object.
     * @return player - represents player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Mutator method to set the player object.
     * @param newPlayer - new player object
     */
    public void setPlayer(Player newPlayer) {
        player = newPlayer;
    }

    /**
     * Accessor method to retrieve the connected door.
     * Based on the specified direction of the room.
     * @param direction - direction of the room to get
     * @return found or null based on the object being found
     */
    public Door getDoor(String direction) {
        return doors.get(direction);
    }

    /**
     * Accessor method to retrieve list of all current doors.
     * @return doorList - list of doors in the room
     */
    public ArrayList<Door> getDoors() {

        for (Map.Entry<String, Door> door : doors.entrySet()) {
            doorList.add(door.getValue());
        }
        return doorList;
    }

    /**
     * Accessor method to set list of all current doors.
     * @param door - new door object
     */
    public void setDoor(Door door) {
        doors.put(door.getDirection(), door);
    }

    /**
     * Method checks if the player is in the current room.
     * @return true if the player is currently in the room
     */
    public boolean isPlayerInRoom() {
        return player.getCurrentRoom() == this;
    }


    /**
     * Verifies the room has at least one door.
     * @return true if no exception was thrown
     * @throws NotEnoughDoorsException in case room has no doors
     */
    public boolean verifyRoom() throws NotEnoughDoorsException {
        if (doors.isEmpty()) {
            throw new NotEnoughDoorsException();
        }
        return true;
    }

    /**
     * Method generates a 2d array representing each tile in the room.
     * @return blocks - 2d array implementation
     */
    private Character[][] makeBlocks() {
        Character[][] blocks = new Character[width][height];

        createFloor(blocks);
        createWalls(blocks);
        createDoors(blocks);
        createItems(blocks);
        createPlayer(blocks);

        return blocks;
    }

    /**
     * Method manipulates Floor symbols as character blocks in display.
     * @param blocks - 2d array implementation
     */
    private void createFloor(Character[][] blocks) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                blocks[x][y] = symbolMap.get("FLOOR");
            }
        }
    }

    /**
     * Method manipulates Walls symbols as character blocks in display.
     * @param blocks - 2d array implementation
     */
    private void createWalls(Character[][] blocks) {
        for (int x = 0; x < height; x++) {
            blocks[0][x] = symbolMap.get("EW_WALL");
            blocks[width - 1][x] = symbolMap.get("EW_WALL");
        }
        for (int x = 0; x < width; x++) {
            blocks[x][0] = symbolMap.get("NS_WALL");
            blocks[x][height - 1] = symbolMap.get("NS_WALL");
        }
    }

    /**
     * Method manipulates Doors symbols as character blocks in display.
     * @param blocks - 2d array implementation
     */
    private void createDoors(Character[][] blocks) {
        for (Map.Entry<String, Door> entry : doors.entrySet()) {
            switch (entry.getKey()) {
                case "N":
                    blocks[entry.getValue().getWallPosition()][0] = symbolMap.get("DOOR");
                    break;
                case "W":
                    blocks[0][entry.getValue().getWallPosition()] = symbolMap.get("DOOR");
                    break;
                case "S":
                    blocks[entry.getValue().getWallPosition()][height - 1] = symbolMap.get("DOOR");
                    break;
                case "E":
                    blocks[width - 1][entry.getValue().getWallPosition()] = symbolMap.get("DOOR");
                    break;
                default:
            }
        }
    }

    /**
     * Method manipulates Items symbols as character blocks in display.
     * @param blocks - 2d array implementation
     */
    private void createItems(Character[][] blocks) {
        for (Item item : roomItems) {
            Point point = item.getXyLocation();
            blocks[point.x][point.y] = symbolMap.get(item.getType().toUpperCase());
        }
    }

    /**
     * Method manipulates Player symbols as character blocks in display.
     * @param blocks - 2d array implementation
     */
    private void createPlayer(Character[][] blocks) {
        if (player.getCurrentRoom() == this) {
            Point playerPos = player.getXyLocation();
            blocks[playerPos.x][playerPos.y] = symbolMap.get("PLAYER");
        }
    }

    /**
     * Method generates a string of each individual block in the grid.
     * based on the dimensions of the room
     * @param blocks - represents each block in the game's grid
     * @return grid - String represents the display of the game
     */
    private String displayBlocks(Character[][] blocks) {
        String grid = "";

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                grid = grid.concat(String.valueOf(blocks[y][x]));
            }
            grid = grid.concat("\n");
        }
        return grid;
    }


    /**
     * Method generates a string that can be printed to produce an ascii.
     * rendering of the room and all of its contents.
     * @return grid - String represents the display of the room
     */
    public String displayRoom() {
        String grid = "";

        Character[][] blocks = makeBlocks();
        grid = grid.concat(displayBlocks(blocks));

        if (isPlayerInRoom()) {
            Point point = player.getXyLocation();
            blocks[(int) point.getX()][(int) point.getY()] = symbolMap.get("PLAYER");
        }
        return grid;
    }
}

