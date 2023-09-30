package ulb.infof307.g9.model.database;

import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.model.database.exceptions.DatabaseExceptionType;
import ulb.infof307.g9.model.database.exceptions.DeckException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DatabaseDeck class for handling the decks in the database.
 * This class is used to insert, update, delete and get decks from the database.
 */
class DatabaseDeck{

    private final boolean serverMode;
    private final DatabaseConnection dbConnection;

    /**
     * @param serverMode say if this database is also destined to the server database
     * @throws DatabaseException if there was a sql error
     */
    public DatabaseDeck(boolean serverMode) throws DatabaseException {
        this.serverMode = serverMode;
        dbConnection = new DatabaseConnection();
        // tags will be stored in a simple text separated by ;
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            Statement statement = connection.createStatement();
            String query = "create table if not exists Deck (name TEXT, " +
                    "tags TEXT, " +
                    "author TEXT, " +
                    "description TEXT, " +
                    "PRIMARY KEY(name));";
            statement.executeUpdate(query);
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to insert a new deck in the database
     *
     * @param deck             object to insert
     * @param databaseDeckCard table that links each card to its corresponding deck
     * @param databaseCard     table that handles the cards
     * @throws DatabaseException if the insertion failed or there was a sql error
     */
    public void insertNewDeck(Deck deck, DatabaseDeckCard databaseDeckCard, DatabaseCard databaseCard) throws DatabaseException {
        try {
            deckExists(deck.getName());
        } catch (DeckException ignored) {   // there is no deck matching the queried deckName, so we can insert one
            try {
                Connection connection = dbConnection.startConnection(serverMode);
                PreparedStatement preparedStatement = connection.prepareStatement("insert into Deck (name, tags) values (?,?)");
                preparedStatement.setString(1, deck.getName());
                preparedStatement.setString(2, deck.getFormattedTags());

                preparedStatement.executeUpdate();

                // if there are cards in the deck we insert them in the database
                if (!deck.getCards().isEmpty()) {
                    databaseDeckCard.insertListOfCardsIntoDeck(deck.getCards(), deck.getName(), databaseCard, this);
                }

                connection.close();
                return; // the return is needed because we will raise an already_exist exception
            } catch (SQLException e) {
                throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
            }
        }
        throw new DeckException(DatabaseExceptionType.ALREADY_EXISTS, deck.getName());
    }

    /**
     * Method used to get a deck from its name
     *
     * @param deckName   of the name to get
     * @param dbDeckCard table linking the cards and the deck
     * @param dbCard     table containing the cards
     * @return the queried deck
     * @throws DatabaseException if there was an error during the retrieval or a sql error
     */
    public Deck getDeck(String deckName, DatabaseDeckCard dbDeckCard, DatabaseCard dbCard) throws DatabaseException {
        deckExists(deckName);
        Deck deck;
        deck = new Deck(deckName, dbDeckCard.getAllCardsFromDeck(deckName, dbCard), getTagsFromDeck(deckName));

        try {
            deck.setDescription(getDescriptionFromDeck(deckName));
        } catch (DeckException ignored) {
            // some deck don't have any description
        }
        return deck;
    }

    /**
     * Method used to get the tags corresponding to a certain deck
     *
     * @param deckName of the deck to retrieve the tags from
     * @return a list of tags
     * @throws DatabaseException if the select failed or there was a sql error
     */
    public List<String> getTagsFromDeck(String deckName) throws DatabaseException {
        deckExists(deckName);
        try {
            List<String> tags;
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select tags from Deck where name=?");
            preparedStatement.setString(1, deckName);

            ResultSet result = preparedStatement.executeQuery();
            if (result.getString(1).isBlank()) {    // if there is no tag
                tags = new ArrayList<>();
            } else {    // if there is at least one tag
                // splitting the formatted tag list (tag1;tag2;...)
                tags = new ArrayList<>(Arrays.asList(result.getString(1).split(";")));
            }
            connection.close();
            return tags;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to check if a deck exists or not from its name
     *
     * @param deckName to check in database
     * @throws DatabaseException if the deck doesn't exist or there was a sql error
     */
    public void deckExists(String deckName) throws DatabaseException {
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select 1 from Deck where name=?");
            preparedStatement.setString(1, deckName);

            ResultSet resultSet = preparedStatement.executeQuery();
            int result = resultSet.getInt(1);
            connection.close();
            //Verify that the nickname is not used by anyone.
            if (result != 1) {
                throw new DeckException(DatabaseExceptionType.NO_SUCH_ENTRY, deckName);
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to change the deck tags
     *
     * @param deckName         of the deck to change
     * @param newFormattedTags new tags
     * @throws DatabaseException if the update failed or there was a sql error
     */
    public void modifyDeckTags(String deckName, String newFormattedTags) throws DatabaseException {
        deckExists(deckName);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("update Deck set tags=? where name=?");
            preparedStatement.setString(1, newFormattedTags);
            preparedStatement.setString(2, deckName);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }


    /**
     * Method used to update a certain deck author
     *
     * @param deckName of the deck to update
     * @param author   of the selected deck
     * @throws DatabaseException if the update failed or there was a sql error
     */
    public void modifyAuthor(String deckName, String author) throws DatabaseException {
        deckExists(deckName);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("update Deck set author=? where name=?");
            preparedStatement.setString(1, author);
            preparedStatement.setString(2, deckName);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to update a certain deck description
     *
     * @param deckName    of the deck to update
     * @param description to insert in the selected deck
     * @throws DatabaseException if the update failed or there was a sql error
     */
    public void modifyDeckDescription(String deckName, String description) throws DatabaseException {
        deckExists(deckName);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("update Deck set description=? where name=?");
            preparedStatement.setString(1, description);
            preparedStatement.setString(2, deckName);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to get the author of a certain deck
     *
     * @param deckName of the deck to get the author from
     * @return the author of the queried deck
     * @throws DatabaseException if the select failed or there was a sql error
     */
    public String getAuthorFromDeck(String deckName) throws DatabaseException {
        deckExists(deckName);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select author from Deck where name=?");
            preparedStatement.setString(1, deckName);
            ResultSet resultSet = preparedStatement.executeQuery();
            String author = resultSet.getString(1);

            connection.close();
            return author;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to get the description from a certain deck
     *
     * @param deckName of the deck to get the description from
     * @return the description of the queried deck
     * @throws DatabaseException if the select failed or there was a sql error
     */
    public String getDescriptionFromDeck(String deckName) throws DatabaseException {
        deckExists(deckName);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select description from Deck where name=?");
            preparedStatement.setString(1, deckName);
            ResultSet resultSet = preparedStatement.executeQuery();
            String description = resultSet.getString(1);

            connection.close();
            return description;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }
}
