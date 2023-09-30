package ulb.infof307.g9.model.database;

import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.User;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.view.AlertBuilder;

import java.util.List;

/**
 The Database class is a facade that provides a simplified interface to a flashcard database.
 * This class uses the Facade design pattern to provide a single point of entry for accessing the database, hiding the underlying complexity of the database from the client code.
 * The Database class is implemented using the Singleton design pattern, ensuring that only a single instance of the class exists at any given time.
 */
public class Database {

    private static Database databaseInstance;

    private DatabaseUser dbUser;
    private DatabaseCard dbCard;
    private DatabaseDeck dbDeck;
    private DatabaseDeckCard dbDeckCard;
    private DatabaseRanking dbRanking;
    private boolean serverMode = false;

    private Database() {
        setDataBase();
    }

    /**
     * This method is used to access the database anywhere from the code as a singleton.
     */
    public static Database getInstance() {
        if (databaseInstance == null) databaseInstance = new Database();

        return databaseInstance;
    }

    /**
     * This method is used to select which database we want
     *
     * @param value whether server is local or global
     */
    public void setServerMode(boolean value) {
        serverMode = value;
        setDataBase();
    }

    /**
     * Method used to instanciate the other database we need for the facade design pattern
     */
    private void setDataBase() {
        try {
            dbUser = new DatabaseUser(serverMode);
            dbCard = new DatabaseCard(serverMode);
            dbDeck = new DatabaseDeck(serverMode);
            dbDeckCard = new DatabaseDeckCard(serverMode);
            dbRanking = new DatabaseRanking(serverMode);
        } catch (DatabaseException e) {
            if (serverMode) {
                e.printHint();
            } else {
                new AlertBuilder()
                        .error()
                        .addHeader("Erreur !")
                        .addContent(e.getMessage())
                        .show();
            }
        }
    }

    /**
     * This method is used to insert a new user into the database.
     *
     * @param nickname of the user to add
     * @param password of the user to add
     * @throws DatabaseException the exception bound to the database
     */
    public void insertNewUser(String nickname, String password) throws DatabaseException {
        dbUser.insertNewUser(nickname, password);
    }


    /**
     * Method used to delete an existing User from the database.
     *
     * @param nickname is used to find the user in the database using a sql query.
     * @throws DatabaseException if the deletion fails or a sql exceptions occurs
     */
    public void deleteUser(String nickname) throws DatabaseException {
        dbUser.deleteUser(nickname);
    }

    /**
     * Method used to modify the password of an existing user in the db.
     *
     * @param nickname    is used to find the user in the db using a sql query
     * @param newPassword is used to change the existing password of the user with it.
     * @throws DatabaseException the password update fails or a sql exception eccurs
     */
    public void modifyUserPassword(String nickname, String newPassword) throws DatabaseException {
        dbUser.modifyUserPassword(nickname, newPassword);
    }

    /**
     * This method is used for testing purposes.
     *
     * @param nickname is used for the query.
     * @throws DatabaseException if fetching the user fails or there is sql exception
     */
    public User getUser(String nickname) throws DatabaseException {
        return dbUser.getUser(nickname);
    }

    /**
     * This method is used to insert a new card into the database.
     * If the card already exists, it will not be inserted.
     *
     * @param card is the card to be inserted in the database.
     */
    public void insertNewCard(Card card) throws DatabaseException {
        dbCard.insertNewCard(card);
    }

    /**
     * Returns a Card object containing all the information about the card from the database with the given title.
     *
     * @param cardTitle is the title of the card to retrieve.
     * @return a Card object
     */
    public Card getCard(String cardTitle) throws DatabaseException {
        return dbCard.getCard(cardTitle);
    }

    /**
     * This method is used to check if a card already exists in the database based on its title.
     *
     * @param title is the title of the card.
     * @throws DatabaseException if there was a sql error
     */
    public void existCard(String title) throws DatabaseException {
        dbCard.cardExists(title);
    }

    /**
     * This method is used to delete a card from the database based on its title.
     *
     * @param title is the title of the card to be deleted.
     */
    public void deleteCard(String title) throws DatabaseException {
        dbCard.deleteCard(title);
    }



    /**
     * This method is used to insert a new deck into the database.
     *
     * @param deck is the deck to add in database
     */
    public void insertNewDeck(Deck deck) throws DatabaseException {
        dbDeck.insertNewDeck(deck, dbDeckCard, dbCard);
    }

    /**
     * Method to retrieve a deck from the database.
     *
     * @return a Deck
     */
    public Deck getDeck(String deckName) throws DatabaseException {
        return dbDeck.getDeck(deckName, dbDeckCard, dbCard);
    }


    /**
     * Method to change the existing tags from a deck
     */
    public void modifyDeckTags(String deckName, String newFormattedTags) throws DatabaseException {
        dbDeck.modifyDeckTags(deckName, newFormattedTags);
    }


    /**
     * Method that removes an existing card from the deck
     */
    public void deleteCardFromDeck(String deckName, String cardName) throws DatabaseException {
        dbDeckCard.deleteCardFromDeck(deckName, cardName, dbCard, dbDeck);
    }

    /**
     * Method that inserts a card into the deck
     */
    public void insertCardIntoDeck(String cardName, String deckName) throws DatabaseException {
        dbDeckCard.insertCardIntoDeck(cardName, deckName, dbCard, dbDeck);
    }

    /**
     * Method to remove a deck from the game.
     * We remove the deck from the deck table and from the DecksCards
     */
    public void deleteDeck(String deckToRemove) throws DatabaseException {
        dbDeckCard.deleteDeck(deckToRemove, dbDeck, dbCard);
    }

    /**
     * Method that stores all the cards from a selected deck and returns them
     *
     * @return a list of cards that belong to the selected deck or null
     */
    public List<Card> getAllCardsFromDeck(String deckName) throws DatabaseException {
        return dbDeckCard.getAllCardsFromDeck(deckName, dbCard);
    }

    /**
     * Method used to get all decks from the database
     *
     * @return a list of decks or null.
     */
    public List<Deck> getAllDecks() throws DatabaseException {
        return dbDeckCard.getAllDecks(dbCard, dbDeck);
    }

    /**
     * Method that validates a user when he logs in
     *
     * @param user user to check the validity
     * @return true if the credentials are fine false if not
     */
    public boolean isLoginValid(User user) {
        return dbUser.isLoginValid(user);
    }

    /**
     * Method used to update author of a deck from the database
     *
     * @param deckName
     * @param author
     */
    public void modifyAuthor(String deckName, String author) throws DatabaseException {
        dbDeck.modifyAuthor(deckName, author);
    }

    /**
     * Method used to update description of a deck from the database
     *
     * @param deckName
     * @param description
     */
    public void modifyDeckDescription(String deckName, String description) throws DatabaseException {
        dbDeck.modifyDeckDescription(deckName, description);
    }

    /**
     * Method used to get the author of a deck from the database
     *
     * @param deckName
     * @return the name of the author as a string
     */
    public String getAuthorFromDeck(String deckName) throws DatabaseException {
        return dbDeck.getAuthorFromDeck(deckName);
    }

    /**
     * Method used to get the description of a deck from the database
     *
     * @param deckName
     * @return the description of the deck as a string
     */
    public String getDescriptionFromDeck(String deckName) throws DatabaseException {
        return dbDeck.getDescriptionFromDeck(deckName);
    }

    /**
     * Gets score of a user for a deck
     *
     * @param deckTitle deck
     * @param username  user
     * @return score
     * @throws DatabaseException if fetching the ranking fails
     */
    public int getScoreFromRanking(String deckTitle, String username) throws DatabaseException {
        return dbRanking.getScore(deckTitle, username);
    }

    /**
     * Updates a score or insert it if it doesn't already exist
     *
     * @param deckTitle deck
     * @param username  user
     * @param score     score
     * @throws DatabaseException if updating the ranking fails
     */
    public void modifyScore(String deckTitle, String username, int score) throws DatabaseException {
        dbRanking.modifyScore(dbDeck,deckTitle, username, score);
    }

    /**
     * deletes score
     *
     * @param deckTitle deck
     * @param username  user
     * @throws DatabaseException if deleting the ranking fails
     */
    public void deleteScoreFromRanking(String deckTitle, String username) throws DatabaseException {
        dbRanking.deleteScoreFromRanking(deckTitle, username);
    }

    /**
     * Gets the scores of all users for a deck, ordered smallest -> greatest.
     *
     * @param deck deck
     * @return list of scores
     * @throws DatabaseException if fetching the rankings fails
     */
    public List<RankRow> getRankingFromDeck(String deck) throws DatabaseException {
        return dbRanking.getRankingFromDeck(deck);
    }


    /**
     * Method used to solely update the score of a card
     *
     * @param cardTitle of the card to update
     * @param score     to insert into the card
     * @throws DatabaseException if the card doesn't exist or there was a sql error
     */
    public void modifyCardScore(String cardTitle, int score) throws DatabaseException {
        dbCard.modifyCardScore(cardTitle, score);
    }

}
