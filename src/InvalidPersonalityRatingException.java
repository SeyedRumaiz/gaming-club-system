/**
 * Exception thrown when a participant provides an invalid rating for a personality question
 */
public class InvalidPersonalityRatingException extends RuntimeException {
    public InvalidPersonalityRatingException(String message) {
        super(message);
    }
}
