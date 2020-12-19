package rogue;

/**
 * Class to intitiate exception when a move within the room is invalid.
 */
public class InvalidMoveException extends Exception {

    /**
     * Default constructor.
     */
    public InvalidMoveException() {
        super();
    }

    /**
     * Constructor including the exception/error message.
     * @param message
     */
    public InvalidMoveException(String message) {
        super(message);
    }
}
