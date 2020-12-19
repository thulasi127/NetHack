# NetHack
A replication of the open source single-player roguelike video game.
By reading into and handling data from JSON files, the game involves several elements such as the environment, entities, and objects which are represented by arrangements of ASCII or Extended ASCII glyphs. In addition to the environment, the interface also displays character and situational information (symbols, items, doors, rooms, etc.).

The bigger picture of this program is that it explores the principle Object-Oriented programming concepts of Object, Class, Inheritance, Polymorphism and Encapsulation. To briefly describe the use of some of these concepts, each object in this game (Item, Player, Door, Room, Rogue) is represented by its own class. The program uses encapsulation to implement the accessor/mutator methods also known as the getters and setters. Finally, the principle of Inheritence is put into use for catch exceptions throughout the code. This project is executed using Gradle and also incorporates JSON file handling and Exception Handlings.

execution + compilation:
$gradle build
$gradle run

4 JSON Files:
A2_Rooms.json
fileLocations.json
rooms-updated.json
symbols-map.json

6 classes:
  WindowUI.java
  RogueParser.java
  Rogue.java
  Item.java
  Player.java
  Door.java
  Room.java

Exception Handling classes:
  InvalidMoveException.java
  ImpossiblePositionException.java
  NoSuchItemException.java
  NotEnoughDoorsException.java

-- Overview of each class --
WindowUI.java:

The overall functionality of this class, is to print the final string representing the entire display, along with providing all user interactions with the Rogue game. The UI actions provided in this class includes:
1. Setting default Window container, layout and terminal
2. Printing a string to the screen starting at the indicated column and row.
3. Clearing and redrawing the whole screen including the room and the message.
4. Obtaining input from the user and returns it as a character. Then converting arrow keys to the equivalent movement keys in rogue.


RogueParser.java:

This class was created to go through each JSON file containing a list of rooms, items, doors and symbols then parse each data, in order to later manipulate them in the Rogue class.

Rogue.java:

This class iterates through each room and item in order to add the room and item objects to the list of rooms/items in the dungeon.

Steps towards doing this:
1. allocate memory for a door/room/item object.
2. set the fields for the new room and item objects using the hash map values contained in RogueParser.
3. add the door/room/item object to the list of rooms/items in the dungeon.
4. add the item to the room it is currently located in.

Item.java:

This class is simply representing them item objects in this Rogue game, with a basic functionality for both consumables and equipment.

1. A method containing field declarations represent the items (id, name, type and location)
2. Setting accessor/mutator methods for each field


Player.java:

This is a simple class with a basic functionality of representing the player character.

1. Method containing field declarations represent the players name, (x,y)location - coordinates, currentRoom
2. Filling in/Setting accessor/mutator methods for each field

Door.java:

This class is simply representing them Door objects in this Rogue game, with a basic functionality of allocating specific attributes to each unique door in a room.

1. A method containing field declarations represent the items (directions (N,S,E,W), positions and list of connected rooms)
2. Setting accessor/mutator methods for each field

Room.java:

The purpose of this class is to draw and form the entire display of the Rogue game by translating the room into ascii values.
Then store and return it as a string. This string will then be used to print all the rooms available. Overall, Class that represents a room within the dungeon and contains items(treasures), players (monsters), doors, etc.

-- Exception Handlings --

InvalidMoveException.java:

Class to handle exception when a move prompted by the user within the room is invalid.

ImpossiblePositionException.java:

Class to handle exception when data stored is placed in the wrong position.

NoSuchItemException.java:

Class to handle exception when item id is not apart of the the given item list.

NotEnoughDoorsException.java:

Class to handle exception when a room does not have any doors.
