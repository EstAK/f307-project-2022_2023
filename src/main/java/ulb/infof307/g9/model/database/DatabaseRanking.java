package ulb.infof307.g9.model.database;

import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.model.database.exceptions.DatabaseExceptionType;
import ulb.infof307.g9.model.database.exceptions.RankingException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The DatabaseRanking class is used to handle the Ranking table in the database.
 */
public class DatabaseRanking{

    private final boolean serverMode;
    private final DatabaseConnection dbConnection;

    /**
     * This Constructor is used to create the Ranking table if it doesn't exist when you call the singleton for the first time.
     * The Ranking table is used to store the scores.
     * It contains the following fields:
     * - deck: the name of the deck (PK)
     * - user: the username of the user (PK)
     * - score: the score of the user for this deck
     *
     * @param serverMode the server mode
     * @throws RankingException when there is an sql error
     */
    public DatabaseRanking(boolean serverMode) throws DatabaseException {
        this.serverMode = serverMode;
        dbConnection = new DatabaseConnection();
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            Statement statement = connection.createStatement();
            String query = "create table if not exists Ranking (" +
                    "deck VARCHAR(255)," +
                    "user VARCHAR(255)," +
                    "score INTEGER," +
                    "PRIMARY KEY (deck, user));";
            statement.executeUpdate(query);
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }


    /**
     * Insert new score in Ranking.
     *
     * @param deckTitle the deck
     * @param username  the user
     * @param score     the score
     * @throws DatabaseException if the insertion fails or the Ranking already exists
     */
    public void insertNewScore(String deckTitle, String username, int score) throws DatabaseException {
        try {
            scoreExists(deckTitle, username);
        } catch (RankingException ignored) {    // there is no ranking for the given combination
            try {
                Connection connection = dbConnection.startConnection(serverMode);
                PreparedStatement preparedStatement = connection.prepareStatement("insert into Ranking values(?,?,?)");
                preparedStatement.setString(1, deckTitle);
                preparedStatement.setString(2, username);
                preparedStatement.setInt(3, score);
                preparedStatement.executeUpdate();

                connection.close();
                return; // the return is needed because we will raise an already_exist exception
            } catch (SQLException e) {
                throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
            }
        }
        // throws an error if the ranking already exists
        throw new RankingException(DatabaseExceptionType.ALREADY_EXISTS, deckTitle, username);
    }


    /**
     * Get the score of a user for a deck
     *
     * @param deckTitle the deck title
     * @param username  the username
     * @return Ranking score or null if there was an error
     * @throws DatabaseException if fetching the ranking failed
     */
    public int getScore(String deckTitle, String username) throws DatabaseException {
        scoreExists(deckTitle, username);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select score from Ranking where deck=? and user=?");
            preparedStatement.setString(1, deckTitle);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            int score = resultSet.getInt(1);
            connection.close();

            return score;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Check if a score already exists for a user and a deck
     *
     * @param deckTitle the deck title
     * @param username  the username
     * @throws DatabaseException if there is no ranking
     */
    public void scoreExists(String deckTitle, String username) throws DatabaseException {
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select 1 from Ranking where deck=? and user=?");
            preparedStatement.setString(1, deckTitle);
            preparedStatement.setString(2, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            int result = resultSet.getInt(1);

            connection.close();
            if (result != 1) throw new RankingException(DatabaseExceptionType.NO_SUCH_ENTRY, deckTitle, username);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Delete score of a user for a deck from Ranking.
     *
     * @param deckTitle the deck title
     * @param username  the username
     * @throws DatabaseException if deleting the ranking failed
     */
    public void deleteScoreFromRanking(String deckTitle, String username) throws DatabaseException {
        scoreExists(deckTitle, username);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("delete from Ranking where deck=? and user=?");
            preparedStatement.setString(1, deckTitle);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Update a score or insert it if it doesn't already exist.
     *
     * @param deckTitle the deck title
     * @param username  the username
     * @param score     the score
     * @throws DatabaseException if updating the ranking failed
     */
    public void modifyScore(DatabaseDeck databaseDeck, String deckTitle, String username, int score) throws DatabaseException {
        // checks if the ranking exists if not it inserts it
        databaseDeck.deckExists(deckTitle);
        try {
            // the user insertion will throw an error if there already is an entry for a certain combination
            insertNewScore(deckTitle, username, score);
        } catch (RankingException ignored) {   // the ranking is already present in the database
            try {
                Connection connection = dbConnection.startConnection(serverMode);
                PreparedStatement preparedStatement = connection.prepareStatement("update Ranking set score=? where deck=? and user=?");
                preparedStatement.setInt(1, score);
                preparedStatement.setString(2, deckTitle);
                preparedStatement.setString(3, username);
                preparedStatement.executeUpdate();

                connection.close();
            } catch (SQLException e) {
                throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
            }
        }
    }

    /**
     * Gets the scores of all users for a deck, ordered smallest -> greatest.
     *
     * @param deck the deck
     * @return all scores
     * @throws DatabaseException if fetching the ranking failed
     */
    public List<RankRow> getRankingFromDeck(String deck) throws DatabaseException {
        List<RankRow> ranking = new ArrayList<>();
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            PreparedStatement preparedStatement = connection.prepareStatement("select row_number() over (order by score desc), user, score from Ranking where deck=?;");
            preparedStatement.setString(1, deck);
            ResultSet resultSet = preparedStatement.executeQuery();

            // goes through all the returned rows for a certain deck
            while (resultSet.next()) {
                RankRow row = new RankRow(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
                ranking.add(row);
            }

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
        return ranking;
    }
}
