package ulb.infof307.g9.model.database.exceptions;
/**
 * The RankingException class is used to handle exceptions that are related to flashcard rankings.

 * The RankingException class extends DatabaseException class, allowing it to be thrown and caught like any other exception.
 */
public class RankingException extends DatabaseException {
    /**
     * Constructor used to create RankingException
     * @param databaseExceptionType the type of databaseException
     * @param decktitle the name of the deck
     * @param username the name of the user
     */
    public RankingException(DatabaseExceptionType databaseExceptionType, String decktitle, String username) {
        super(databaseExceptionType, switch (databaseExceptionType) {
            case NO_SUCH_ENTRY ->
                    String.format("there is no ranking for the user '%s' on the deck '%s'", username, decktitle);
            case ALREADY_EXISTS ->
                    String.format("there already exists a ranking from user '%s' for deck '%s'", username, decktitle);
            default -> "Unexpected value: " + databaseExceptionType;
        });
    }
}
