package rogue;
import java.awt.Point;

/**
 * A basic Item class; basic functionality for both consumables and equipment.
 */
public class Item {
    private int itemId;
    private String itemName;
    private String itemType;
    private Point itemXYlocation;

    /**
     * Default item constructor.
     */
    public Item() {
        itemXYlocation = new Point();
        itemId = 0;
        itemName = "";
        itemType = "";
    }

    /**
     * Item constructor with all data provided.
     *
     * @param id - id of the item
     * @param name  - name of the item
     * @param type  - type of the item
     * @param xyLocation - x,y coordinates to represent item's location
     */
    public Item(int id, String name, String type, Point xyLocation) {
        itemId = id;
        itemName = name;
        itemType = type;
        itemXYlocation = xyLocation;
    }

    /**
     * Accessor method to retrieve item's id.
     * @return itemId
     */
    public int getId() {
        return itemId;
    }

    /**
     * Mutator method to set item's id.
     * @param id - represents the new item id
     */
    public void setId(int id) {
        itemId = id;
    }

    /**
     * Accessor method to retrieve item's name.
     * @return itemName
     */
    public String getName() {
        return itemName;
    }

    /**
     * Mutator method to set item's id.
     * @param name - represents the new item name
     */
    public void setName(String name) {
        itemName = name;
    }


    /**
     * Accessor method to retrieve item's type.
     * @return itemType
     */
    public String getType() {
        return itemType;
    }

    /**
     * Mutator method to set item's type.
     * @param type - represents the new item type
     */
    public void setType(String type) {
        itemType = type;
    }

    /**
     * Accessor method to retrieve item's xy coordinate location.
     * @return itemXYlocation
     */
    public Point getXyLocation() {
        return itemXYlocation;
    }

    /**
     * Mutator method to set item's location.
     * @param newXYlocation - represents the item's new xy coordinate location
     */
    public void setXyLocation(Point newXYlocation) {
        itemXYlocation = newXYlocation;
    }
}
