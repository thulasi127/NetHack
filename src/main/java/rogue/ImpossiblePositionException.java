package rogue;
import java.awt.Point;

/**
 * Class to intitiate exception when data stored is placed in the wrong position.
 */
public class ImpossiblePositionException extends Exception {
    private Point pos;
    private Room room;

    /**
     * Default constructor.
     */
    public ImpossiblePositionException() {
        this(-1, -1, null);
    }

    /**
     * Constructor representing exception or when impossible position is stored in the room.
     * @param x         - x value of the position
     * @param y         - y value of the position
     * @param posRoom - room associated with position
     */
    public ImpossiblePositionException(int x, int y, Room posRoom) {
        pos = new Point(x, y);
        room = posRoom;
    }

    /**
     * Accessor method that retrieves for the stored position.
     * @return pos
     */
    public Point getPosition() {
        return pos;
    }

    /**
     * Accessor method that retrieves the stored room.
     * @return room
     */
    public Room getRoom() {
        return room;
    }
}
