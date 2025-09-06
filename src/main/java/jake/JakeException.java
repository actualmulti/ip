package jake;

/**
 * Custom exception class for Jake application errors.
 * Extends RuntimeException to handle application-specific error conditions.
 */
public class JakeException extends RuntimeException {
    /**
     * Creates a new JakeException with the specified error message.
     *
     * @param message the error message describing the exception
     */
    public JakeException(String message) {
        super(message);
    }
}
