package ulb.infof307.g9.model.database.exceptions;

/**
 * The UserException class is used to handle exceptions that are related to flashcard users.
 * The UserException class extends the  DatabaseException class, allowing it to be thrown and caught like any other exception.
 */
public class UserException extends DatabaseException {
    /**
     * Constructor used to create UserException
     * @param databaseExceptionType the type of databaseException
     * @param username the name of the user
     */
    public UserException(DatabaseExceptionType databaseExceptionType, String username) {
        super(databaseExceptionType, switch (databaseExceptionType) {
            case ALREADY_EXISTS -> String.format("there already exists a user with the username '%s'", username);
            default -> "Unexpected value: " + databaseExceptionType.toString();
        });
    }

}
