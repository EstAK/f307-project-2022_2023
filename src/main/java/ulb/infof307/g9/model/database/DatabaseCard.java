package ulb.infof307.g9.model.database;

import ulb.infof307.g9.model.CardType;
import ulb.infof307.g9.model.cards.Card;
import ulb.infof307.g9.model.Modular;
import ulb.infof307.g9.model.cards.HoleCard;
import ulb.infof307.g9.model.cards.MultiChoiceCard;
import ulb.infof307.g9.model.cards.StandardCard;
import ulb.infof307.g9.model.database.exceptions.CardException;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.model.database.exceptions.DatabaseExceptionType;

import java.sql.*;
import java.util.Random;

/**
 * The DatabaseCard class represents the card table in the flashcard database and provides methods to manipulate the data.
 */
class DatabaseCard{
    private final DatabaseConnection dbConnection;
    private static final String SEPARATOR = "\u001F";
    private final boolean serverMode;
    /**
     * This Constructor is used to create the Card table if it doesn't exist when you call the singleton for the first time.
     * The Card table is used to store the cards.
     * It contains the following fields:
     * - title: the title of the card (PK)
     * - question: the question of the card
     * - answer: the answer of the card
     * - score: the score of the card
     * - nbrPicked: the number of times the card has been picked
     * @param serverMode say if this database is also destined to the server database
     */
    public DatabaseCard(boolean serverMode) throws DatabaseException {
        this.serverMode = serverMode;
        dbConnection = new DatabaseConnection();
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            Statement statement = connection.createStatement();
            String query = "create table if not exists Card (" +
                    "title VARCHAR(255)," +
                    "question VARCHAR(255)," +
                    "answer VARCHAR(255)," +
                    "score INTEGER," +
                    "nbrPicked INTEGER," +
                    "type VARCHAR(255)," +
                    "PRIMARY KEY (title));";
            statement.executeUpdate(query);
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to insert a card in the database
     *
     * @param card to insert
     * @throws CardException if there already is a card with the same name or there was sql error
     */
    public void insertNewCard(Card card) throws DatabaseException {
        try {
            cardExists(card.getTitle());
        } catch (CardException ignored) {   // there is no card with the same title
            try {
                Connection connection = dbConnection.startConnection(serverMode);
                PreparedStatement preparedStatement = connection.prepareStatement("insert into Card values(?,?,?,?,?,?)");
                preparedStatement.setString(1, card.getTitle());
                preparedStatement.setString(2, card.getQuestion());
                preparedStatement.setString(3, generateFormattedAnswer(card));
                preparedStatement.setInt(4, card.getScore());
                preparedStatement.setInt(5, card.getNbrPicked());
                preparedStatement.setString(6, card.getType().toString());
                preparedStatement.executeUpdate();

                connection.close();
                return; // the return is needed because we will raise an already_exist exception
            } catch (SQLException e) {
                throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
            }
        }
        throw new CardException(DatabaseExceptionType.ALREADY_EXISTS, card.getTitle());
    }

    /**
     * when using this method you have to cast the return value to the correct type either CardChoice or Card
     *
     * @param cardTitle of the queried card
     * @return Card the queried card
     * @throws DatabaseException if the card doesn't exist or there was a sql error
     */
    public Card getCard(String cardTitle) throws DatabaseException {
        cardExists(cardTitle);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            Card card;
            PreparedStatement preparedStatement = connection.prepareStatement("select * from Card where title=?");
            preparedStatement.setString(1, cardTitle);
            ResultSet result = preparedStatement.executeQuery();

            // constructing the card

            String type = result.getString(6);
            // should add a new column for mod instead of whole new type
            if (type.contains("MULTI")) {
                String[] answers = result.getString(3).split(SEPARATOR);
                card = new MultiChoiceCard(result.getString(1), result.getString(2), answers[0], answers[1], answers[2], answers[3], result.getInt(4), 0, result.getInt(5), CardType.valueOf(type));
            } else if (type.contains("NORMAL")) {
                card = new StandardCard(result.getString(1), result.getString(2), result.getString(3), result.getInt(4), 0, result.getInt(5), CardType.valueOf(type));
            } else {
                card = new HoleCard(result.getString(1), result.getString(2), result.getString(3), result.getInt(4), 0, result.getInt(5));
            }

            connection.close();
            return card;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to check if a card exists
     *
     * @param title of the card to check in the database
     * @throws DatabaseException if the select failed or there was a sql error
     */
    public void cardExists(String title) throws DatabaseException {
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select 1 from Card where title=?");
            preparedStatement.setString(1, title); //replace 1 by the title in the string
            ResultSet resultSet = preparedStatement.executeQuery();
            int result = resultSet.getInt(1);
            connection.close();

            if (result != 1) {
                //if the card exists already in the database
                throw new CardException(DatabaseExceptionType.NO_SUCH_ENTRY, title);
            }
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to delete a certain card
     *
     * @param title of the card to delete
     * @throws DatabaseException if the card doesn't exist or there was a sql error
     */
    public void deleteCard(String title) throws DatabaseException {
        cardExists(title);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("delete from Card where title=?");
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Generate formatted answer for the database
     * @param card the card
     * @return the answer formatted
     */
    private static String generateFormattedAnswer(Card card) throws CardException {
        if (card.getType() == CardType.MULTI_CHOICE || card.getType() == CardType.MODULAR_MULTI) {
            MultiChoiceCard multiChoiceCard = (MultiChoiceCard) card;
            if (multiChoiceCard.getAnswer().contains(SEPARATOR)
                    || multiChoiceCard.getFake1().contains(SEPARATOR)
                    || multiChoiceCard.getFake2().contains(SEPARATOR)
                    || multiChoiceCard.getFake3().contains(SEPARATOR)) {
                throw new CardException("Une carte à choix multiples ne peut pas contenir le caractère 'U+%04x' dans ses réponses.".formatted(SEPARATOR.codePointAt(0)));
            }
            String[] answers = {multiChoiceCard.getAnswer(), multiChoiceCard.getFake1(), multiChoiceCard.getFake2(), multiChoiceCard.getFake3()};
            return String.join(SEPARATOR, answers);
        }
        return card.getAnswer();
    }


    /**
     * Method used to solely update the score of a certain card
     * @param cardTitle of the card to modify
     * @param score to insert into the card
     * @throws DatabaseException if the card doesn't exist or there was a sql error
     */
    public void modifyCardScore(String cardTitle, int score) throws DatabaseException {
        cardExists(cardTitle);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("update card set score=? where title=?");
            preparedStatement.setInt(1, score);
            preparedStatement.setString(2, cardTitle);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }



}
