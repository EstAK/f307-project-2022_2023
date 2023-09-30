package ulb.infof307.g9.model.database.exceptions;

/**
 * The CardException class is used to handle exceptions that are related to flashcards.
 * This can include exceptions such as a card not being found, a card being invalid, or an error occurring while reading or writing card data.
 * The CardException class extends the DatabaseException class, allowing it to be thrown and caught like any other exception.
 */
public class CardException extends DatabaseException{
    /**
     * Method used to handle exceptions
     * @param databaseExceptionType the type of databaseException
     * @param cardTitle the title of the card
     */
    public CardException(DatabaseExceptionType databaseExceptionType, String cardTitle) {
        super(databaseExceptionType, switch (databaseExceptionType) {
            case ALREADY_EXISTS -> String.format("Il existe déjà une carte avec le titre : '%s'", cardTitle);
            case NO_SUCH_ENTRY -> String.format("Il n'existe pas de carte ayant titre : '%s'", cardTitle);
            default -> ("Unexpected value: " + databaseExceptionType);
        });
    }

    public CardException(String message) {
        super(DatabaseExceptionType.DB_EXCEPTION, message);
    }
}
