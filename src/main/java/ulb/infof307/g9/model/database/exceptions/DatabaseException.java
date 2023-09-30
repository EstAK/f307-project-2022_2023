package ulb.infof307.g9.model.database.exceptions;

import java.sql.SQLException;
import java.time.LocalDateTime;
/**
 * The DatabaseException class is used to handle exceptions that are related to a flashcard database.
 * The class extends the Exception class, allowing it to be thrown and caught like any other exception.
 */
public class DatabaseException extends Exception {
    private final DatabaseExceptionType type;
    private final LocalDateTime localDateTime = LocalDateTime.now();

    /**
     * Constructor printing a hint error message
     *
     * @param databaseExceptionType the type of databaseException
     * @param message               to display as hint
     */
    public DatabaseException(DatabaseExceptionType databaseExceptionType, String message) {
        super(message);
        this.type = databaseExceptionType;
    }

    /**
     * Constructor using SQLException instead of message
     *
     * @param databaseExceptionType the type of databaseException
     * @param exception             to get the message from
     */
    public DatabaseException(DatabaseExceptionType databaseExceptionType, SQLException exception) {
        this(databaseExceptionType, exception.getMessage());
        exception.printStackTrace();
    }

    /**
     * Prints a formatted hint to the terminal
     */
    public void printHint() {
        System.out.printf(getHintMessage());
    }

    /**
     * Get the hint message in a formatted string
     *
     * @return the hint message
     */
    public String getHintMessage() {

        return String.format("%s@[%s] from %s:%s\n", getType().toString(),
                getLocalDateTime().toString(),
                getClass().getSimpleName(), getMessage());

    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public DatabaseExceptionType getType() {
        return type;
    }
}
