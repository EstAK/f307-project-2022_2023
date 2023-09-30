package ulb.infof307.g9.model.database.exceptions;
/**
 * The DeckCardException class is used to handle exceptions that are related to the link between the card and the deck.
 * The DeckCardException class extends the DatabaseException class, allowing it to be thrown and caught like any other exception.
 */
public class DeckCardException extends DatabaseException {
    /**
     * Constructor used to create DeckCardException
     * @param databaseExceptionType the type of databaseException
     * @param cardTitle the title of the card
     * @param deckName the name of the deck
     */
    public DeckCardException(DatabaseExceptionType databaseExceptionType, String cardTitle, String deckName) {
        super(databaseExceptionType, switch (databaseExceptionType) {
            case NO_SUCH_ENTRY ->
                    String.format("there is no link between the card '%s' and the deck '%s'", cardTitle, deckName);
            case ALREADY_EXISTS ->
                    String.format("there already is a link between the card '%s' and the deck '%s'", cardTitle, deckName);
            default -> ("Unexpected value: " + databaseExceptionType);
        });
    }
}
