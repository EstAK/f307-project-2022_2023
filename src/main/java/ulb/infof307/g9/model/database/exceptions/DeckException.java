package ulb.infof307.g9.model.database.exceptions;

/**
 * The DeckCardException class is used to handle exceptions that are related to a flashcard deck.
 * The DeckCardException class extends the DatabaseException class, allowing it to be thrown and caught like any other exception.
 */
public class DeckException extends DatabaseException {
    /**
     * Constructor used to create DeckCardException
     * @param databaseExceptionType the type of databaseException
     * @param deckName the name of the deck
     */
    public DeckException(DatabaseExceptionType databaseExceptionType, String deckName) {
        super(databaseExceptionType, switch (databaseExceptionType) {
            case NO_SUCH_ENTRY -> String.format("there is no such deck with the name '%s'", deckName);
            case ALREADY_EXISTS -> String.format("there already exist a deck '%s'", deckName);
            default -> ("Unexpected value: " + databaseExceptionType);
        });
    }
}
