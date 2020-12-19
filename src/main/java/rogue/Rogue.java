package rogue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import java.awt.Point;

/**
 * Class that acts as the moderator of the Rogue game.
 * Retrives and manipulates data from the given json files
 */
public class Rogue {
    // given static variables
    public static final char UP = 'h';
    public static final char DOWN = 'j';
    public static final char LEFT = 'k';
    public static final char RIGHT = 'l';

    private RogueParser parser;
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private Map<String, Character> symbols;
    private Player player = new Player();
    private Room currentRoom;
    private int itemId;
    private String itemName;
    private String itemType;
    private int width;
    private int height;


    /**
     * Default constructor.
     */
    public Rogue() {
    }

    /**
     * Constructor calls the RogueParser to parse through the rooms and items of the game.
     * @param theDungeonInfo - the parser
     */
    public Rogue(RogueParser theDungeonInfo) {

        parser = theDungeonInfo;
        symbols = theDungeonInfo.getSymbols();

        Map roomInfo = parser.nextRoom();
        while (roomInfo != null) {
            addRoom(roomInfo);
            roomInfo = parser.nextRoom();
        }
        parser.resetRoomIterator();
        roomInfo = parser.nextRoom();
        while (roomInfo != null) {
            addDoors(roomInfo);
            roomInfo = parser.nextRoom();
        }
        verifyRooms();

        Map itemInfo = parser.nextItem();
        while (itemInfo != null) {
            try {
                addItem(itemInfo);
            } catch (ImpossiblePositionException e) {
                try {
                    addItem(itemInfo, nextPosition(e.getPosition(), e.getRoom()));
                } catch (ImpossiblePositionException | NoSuchItemException impossiblePositionException) {
                }
            } catch (NoSuchItemException e) {
            }
            itemInfo = parser.nextItem();
        }

        for (Room room : rooms) {
            if (room.getIsStartingRoom()) {
                currentRoom = room;
                player.setCurrentRoom(currentRoom);
                break;
            }
        }
    }

     /**
     * Mutator method to set player.
     * @param thePlayer - game's player
     */
    public void setPlayer(Player thePlayer) {
        player.setName(thePlayer.getName());
        player.setXyLocation(thePlayer.getXyLocation());
    }

    /**
     * Accessor method to retrieve list of rooms.
     * @return rooms
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Accessor method to retrieve a list of items.
     * @return items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Accessor method to retrieve Rogue game's player.
     * @return player
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Called on each key pressed by the user to process.
     * character movement.
     *
     * @param input - last entered character by the user.
     * @return message regarding movement success or failure
     * @throws InvalidMoveException - thrown if input character is not valid
     */
    public String makeMove(char input) throws InvalidMoveException {
        Point playerLoc = player.getXyLocation();
        int playerX = (int) playerLoc.getX();
        int playerY = (int) playerLoc.getY();
        if ((input != RIGHT) && (input != LEFT) && (input != UP) && (input != DOWN)) {
            throw new InvalidMoveException();
        }

        if (input == UP) {
            playerY -= 1;
        } else if (input == DOWN) {
            playerY += 1;
        } else if (input == LEFT) {
            playerX -= 1;
        } else if (input == RIGHT) {
            playerX += 1;
        }

        if (roomExceeded(playerX, playerY)) {
            Door theDoor = findDoor(input, playerX, playerY);

            if (theDoor != null) {
                currentRoom = theDoor.getOtherRoom(currentRoom);
                playerX = 1;
                playerY = 1;
            }
        }
        if (!roomExceeded(playerX, playerY)) {
            player.setXyLocation(new Point(playerX, playerY));
            player.setCurrentRoom(currentRoom);

            ArrayList<Item> currentRoomItems = currentRoom.getRoomItems();
            Iterator<Item> itemIterator = currentRoomItems.iterator();

            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();

                if (item.getXyLocation().equals(player.getXyLocation())) {
                    itemIterator.remove();
                    break;
                }
            }
            currentRoom.setRoomItems(currentRoomItems);
        }
        return "That's a lovely move: " + Character.toString(input);
    }

    /**
     * Generates current room string representation.
     *
     * @return string representation of the current room
     */
    public String getNextDisplay() {
        return currentRoom.displayRoom();
    }

    /**
     * Add a new room to the list of available rooms.
     *
     * @param toAdd - map of a room properties to read from
     */
    public void addRoom(Map<String, String> toAdd) {
        Room room = new Room();

        room.setId(Integer.parseInt(toAdd.get("id")));
        room.setIsStartingRoom(Boolean.parseBoolean(toAdd.get("start")));
        room.setHeight(Integer.parseInt(toAdd.get("height")));
        room.setWidth(Integer.parseInt(toAdd.get("width")));

        room.setPlayer(player);
        room.setSymbols(symbols);

        rooms.add(room);
    }

    /**
     * Updates all rooms information about available doors.
     *
     * @param toAdd - map of a room properties to read from
     */
    public void addDoors(Map<String, String> toAdd) {
        Room room = findRoom(Integer.parseInt(toAdd.get("id")));

        String[] directions = {"N", "S", "W", "E"};
        for (String dir : directions) {
            int wallPosition = Integer.parseInt(toAdd.get(dir + "_wall_pos"));

            if (wallPosition != -1) {
                Door newDoor = new Door(dir, wallPosition);
                Room newRoom = findRoom(Integer.parseInt(toAdd.get(dir + "_con_room")));

                newDoor.connectRoom(room);
                newDoor.connectRoom(newRoom);
                room.setDoor(newDoor);
            }
        }
    }

    /**
     * Add an item information to the list of available items.
     * @param toAdd - map of an item properties to read from
     * @throws ImpossiblePositionException in case item position in room is invalid
     * @throws NoSuchItemException         in case item id is not present among the list of game items
     */
    public void addItem(Map<String, String> toAdd) throws
            ImpossiblePositionException,
            NoSuchItemException {
        addItem(toAdd, null);
    }

     /**
     * Adding a new item to the list of available rooms using Inheritance.
     * @param toAdd - map of an item properties to read from
     * @param position
     * @throws ImpossiblePositionException - in case item position in room is invalid
     * @throws NoSuchItemException - in case item id is not present among the list of game items
     */
    private void addItem(Map<String, String> toAdd, Point position) throws
    ImpossiblePositionException, NoSuchItemException {
        itemId = Integer.parseInt(toAdd.get("id"));
        itemName = toAdd.get("name");
        itemType = toAdd.get("type");

        if (toAdd.get("room") == null) {
            throw new NoSuchItemException();
        }
        int roomID = Integer.parseInt(toAdd.get("room"));
        int x = position == null ? Integer.parseInt(toAdd.get("x")) : position.x;
        int y = position == null ? Integer.parseInt(toAdd.get("y")) : position.y;

        Room room = findRoom(roomID);
        if (roomExceeded(x, y, room)) {
            throw new ImpossiblePositionException(x, y, room);
        }

        Item item = new Item(itemId, itemName, itemType, new Point(x, y));
        items.add(item);

        ArrayList<Item> roomItems = room.getRoomItems();
        roomItems.add(item);
        room.setRoomItems(roomItems);
    }

    /**
     * Basic method to check if the room limit has been exceeded.
     * @param x - Checking size of room through its width
     * @param y - Checking size of room through its height
     * @return roomExceeded - function that compares the room's height and width
     */
    private Boolean roomExceeded(int x, int y) {
        return roomExceeded(x, y, currentRoom);
    }

    /**
     * Method using inheritance to check if the room has exceed its size.
     * by comparing its height and width
     *  @param x - room width counter
     *  @param y - room height counter
     *  @param room - Room object
     *  @return based on room case
     */
    private Boolean roomExceeded(int x, int y, Room room) {
        width = room.getWidth();
        height = room.getHeight();
        return (x == 0 || x == width - 1 || y == 0 || y == height - 1);
    }

    /**
     * Method to find specific room based on the room ID.
     * @param roomID - specific room ID
     * @return room or null - based on the room being found
     */
    private Room findRoom(int roomID) {
        for (Room room : rooms) {
            if (room.getId() == roomID) {
                return room;
            }
        }
        return null;
    }

    /**
     * Method to find door in current room.
     * @param input - User's prompted input/controls
     * @param playerX - Player's x position in the current room
     * @param playerY - Player's y position in the current room
     * @return theDoor - the current door
     */
    private Door findDoor(char input, int playerX, int playerY) {
        Door theDoor = null;

        if (input == UP) {
            Door door = currentRoom.getDoor("N");
            if (door != null && door.getWallPosition() == playerX) {
                theDoor = door;
            }
        } else if (input == DOWN) {
            Door door = currentRoom.getDoor("S");
            if ((door != null) && (door.getWallPosition() == playerX)) {
                theDoor = door;
            }
        } else if (input == LEFT) {
            Door door = currentRoom.getDoor("W");
            if ((door != null) && (door.getWallPosition() == playerY)) {
                theDoor = door;
            }
        } else if (input == RIGHT) {
            Door door = currentRoom.getDoor("E");
            if ((door != null) && (door.getWallPosition() == playerY)) {
                theDoor = door;
            }
        }

        return theDoor;
    }

    /**
     * Method to find room's next position.
     * @param position
     * @param room
     * @return nextPos - the room's next position
     */
    private Point nextPosition(Point position, Room room) {
        Point nextPos = new Point(position.x, position.y);

        if (position.x <= 0) {
            nextPos.x = 1;
        } else if (position.x >= room.getWidth() - 1) {
            nextPos.x = room.getWidth() - 2;
        }

        if (position.y <= 0) {
            nextPos.y = 1;
        } else if (position.y >= room.getHeight() - 1) {
            nextPos.y = room.getHeight() - 2;
        }

        return nextPos;
    }

    /**
     * Verifies the room has at least one door.
     */
    private void verifyRooms() {
        for (Room room : rooms) {
            try {
                room.verifyRoom();
            } catch (NotEnoughDoorsException e) {
                boolean corrected = false;

                for (Room otherRoom : rooms) {
                    if (createDoor(room, otherRoom)) {
                        corrected = true;
                        break;
                    }
                }

                if (!corrected) {
                    System.out.println("The dungeon file cannot be used as itemIterator contains invalid room");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Method verifies if a new door can be created.
     * @param roomA
     * @param roomB
     * @return true if new door can be created
     */
    private boolean createDoor(Room roomA, Room roomB) {
        if (roomA == roomB) {
            return false;
        }

        String[] direction = {"N", "S", "W", "E"};
        if ((roomA.getDoors().size()) >= (direction.length) || (roomB.getDoors().size()) >= (direction.length)) {
            return false;
        }

        String doorADir = locateMissingDoor(roomA);

        Door door1 = new Door(doorADir, 1);
        door1.connectRoom(roomA);
        door1.connectRoom(roomB);
        roomA.setDoor(door1);

        String doorBDir = locateMissingDoor(roomB);

        Door door2 = new Door(doorBDir, 1);
        door2.connectRoom(roomA);
        door2.connectRoom(roomB);
        roomB.setDoor(door2);

        return true;
    }

    /**
     * Method locates missing doors in a room.
     * @param room -Room object
     * @return doorDir - location of missing door
     */
    private String locateMissingDoor(Room room) {
        Boolean found;
        String[] direction = {"N", "S", "W", "E"};

        String doorDir = null;
        for (String dir : direction) {
            found = false;
            for (Door door : room.getDoors()) {
                if (dir.equals(door.getDirection())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                doorDir = dir;
                break;
            }
        }
        return doorDir;
    }

    /**
     * Method generates a string that concatenates with displayRoom.
     * @return add - String representing entire game display
     */
    public String displayAll() {
        String add = "";

        for (Room room : rooms) {
            add = add.concat(room.displayRoom());
            add = add.concat("\n\n");
        }
        return add;
    }
}
