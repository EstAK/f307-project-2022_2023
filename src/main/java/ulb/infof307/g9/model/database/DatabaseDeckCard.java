package ulb.infof307.g9.model.database;

import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.database.exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseDeckCard class for managing the link between the deck and the cards.
 */
class DatabaseDeckCard{

    private final boolean serverMode;
    private final DatabaseConnection dbConnection;

    /**
     * This method is used to create the DecksCards table if it doesn't exist when you call the singleton for the first time.
     * The DecksCards table is used to store the cards in the decks.
     * It contains the following fields:
     * - card_title: the title of the card (PK) (FK Card.title)
     * - deck_name: the name of the deck (PK) (FK Deck.name)
     */
    public DatabaseDeckCard(boolean serverMode) throws DatabaseException {
        this.serverMode = serverMode;
        dbConnection = new DatabaseConnection();
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS DecksCards (" +
                    "card_title VARCHAR(255)," +
                    "deck_name VARCHAR(255)," +
                    "PRIMARY KEY (card_title, deck_name)," +
                    "FOREIGN KEY (deck_name) REFERENCES Deck(name) ON DELETE CASCADE," +
                    "FOREIGN KEY (card_title) REFERENCES Card(title) ON DELETE CASCADE" +
                    ");";
            statement.execute(query);
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to remove a card from a deck
     *
     * @param deckName from where to remove a certain card
     * @param cardName to remove from a certain deck
     * @param dbCard   card table
     * @param dbDeck   deck table
     * @throws DatabaseException if the deck, card or their link don't exist or there was a sql error
     */
    public void deleteCardFromDeck(String deckName, String cardName, DatabaseCard dbCard, DatabaseDeck dbDeck) throws DatabaseException {
        cardLinkToDeckExists(cardName, deckName, dbCard, dbDeck);
        // won't go past here if the deck, the card and their link don't exist
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("delete from DecksCards where card_title=? and deck_name=?");
            preparedStatement.setString(1, cardName);
            preparedStatement.setString(2, deckName);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to check if there is a link between a certain card and a deck
     *
     * @param cardTitle    of the card to check the link of
     * @param deckName     of the deck to check the link of
     * @param databaseCard card table
     * @param databaseDeck deck table
     * @throws DatabaseException if there was a sql error or the link doesn't exist
     */
    public void cardLinkToDeckExists(String cardTitle, String deckName, DatabaseCard databaseCard, DatabaseDeck databaseDeck) throws DatabaseException {
        databaseCard.cardExists(cardTitle);
        databaseDeck.deckExists(deckName);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select 1 from DecksCards where card_title=? and deck_name=?");
            preparedStatement.setString(1, cardTitle);
            preparedStatement.setString(2, deckName);
            ResultSet resultSet = preparedStatement.executeQuery();

            int result = resultSet.getInt(1);
            connection.close();

            if (result != 1) {
                throw new DeckCardException(DatabaseExceptionType.NO_SUCH_ENTRY, cardTitle, deckName);
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to link a card to a deck
     *
     * @param cardName to link to a deck
     * @param deckName to add the card to
     * @param dbCard   card table
     * @param dbDeck   deck table
     * @throws DatabaseException if there already is a link between the given card and deck or there was a sql error
     */
    public void insertCardIntoDeck(String cardName, String deckName, DatabaseCard dbCard, DatabaseDeck dbDeck) throws DatabaseException {
        try {
            cardLinkToDeckExists(cardName, deckName, dbCard, dbDeck);
        } catch (DatabaseException ignored) {   // there is no link between cardName and deckName
            try {
                Connection connection = dbConnection.startConnection(serverMode);
                PreparedStatement preparedStatement = connection.prepareStatement("insert into DecksCards values(?,?)");
                preparedStatement.setString(1, cardName);
                preparedStatement.setString(2, deckName);
                preparedStatement.executeUpdate();

                connection.close();
                return; // the return is needed because we will raise an already_exist exception
            } catch (SQLException e) {
                throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
            }
        }
        throw new DeckCardException(DatabaseExceptionType.ALREADY_EXISTS, cardName, deckName);
    }

    /**
     * Wrapper for insertCardIntoDeck
     *
     * @param cards        list of cards to insert
     * @param deckName     deck to insert in
     * @param databaseCard card table
     * @param databaseDeck deck table
     */
    public void insertListOfCardsIntoDeck(List<Card> cards, String deckName, DatabaseCard databaseCard, DatabaseDeck databaseDeck) throws DatabaseException {
        for (Card card : cards) {
            try {   // we try to insert a new card
                databaseCard.insertNewCard(card);
            } catch (DeckException | CardException ignored) {   // the card already exist in de card table
            } finally { // whether there was an exception or not we try to insert the card in the deck
                try {
                    insertCardIntoDeck(card.getTitle(), deckName, databaseCard, databaseDeck);
                } catch (DeckException | CardException ignored) {
                    // there is already a link between the card and deck
                }
            }
        }
    }

    /**
     * Method used to remove a deck
     *
     * @param deckToRemove form the database
     * @param dbDeck       deck table
     * @param dbCard       deck
     * @throws DatabaseException if the deck doesn't exist or there was a sql error
     */
    public void deleteDeck(String deckToRemove, DatabaseDeck dbDeck, DatabaseCard dbCard) throws DatabaseException {
        dbDeck.deckExists(deckToRemove);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            /*
               removes the link between deckToRemove and all its cards
               PreparedStatement firstPreparedStatement = connection.prepareStatement("delete from DecksCards where deck_name=?");
               firstPreparedStatement.setString(1, deckToRemove);
               firstPreparedStatement.executeUpdate();
            */
            for (Card card : getAllCardsFromDeck(deckToRemove, dbCard)) {
                deleteCardFromDeck(deckToRemove, card.getTitle(), dbCard, dbDeck);
                dbCard.deleteCard(card.getTitle());
            }

            PreparedStatement secondPreparedStatement = connection.prepareStatement("delete from Deck where name=?;");
            secondPreparedStatement.setString(1, deckToRemove);
            secondPreparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to fetch all the cards linked to a certain deck that exists
     *
     * @param deckName of the deck to fetch the cards from
     * @param dbCard   card table
     * @return a list of cards
     * @throws DatabaseException if there was a sql error
     */
    public List<Card> getAllCardsFromDeck(String deckName, DatabaseCard dbCard) throws DatabaseException {
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            List<Card> listOfCards = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from DecksCards where deck_name=?");
            preparedStatement.setString(1, deckName);
            ResultSet cards = preparedStatement.executeQuery();

            // parsing through every returned row
            while (cards.next()) {
                listOfCards.add(dbCard.getCard(cards.getString(1)));
            }
            connection.close();
            return listOfCards;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to fetch all the decks stored in the database
     *
     * @param dbCard card table
     * @param dbDeck deck table
     * @return a list of decks
     * @throws DatabaseException if there was a sql error
     */
    public List<Deck> getAllDecks(DatabaseCard dbCard, DatabaseDeck dbDeck) throws DatabaseException {
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            List<Deck> decks = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from Deck");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {  // parsing all entries of Deck table
                String deckName = resultSet.getString(1);  // Deck primary key
                Deck deck = dbDeck.getDeck(deckName, this, dbCard);
                decks.add(deck);
            }

            connection.close();
            return decks;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }
}
