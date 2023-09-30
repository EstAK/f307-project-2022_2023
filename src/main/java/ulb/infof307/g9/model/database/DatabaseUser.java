package ulb.infof307.g9.model.database;

import ulb.infof307.g9.model.User;
import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.model.database.exceptions.DatabaseExceptionType;
import ulb.infof307.g9.model.database.exceptions.UserException;

import java.sql.*;

/**
 * DatabaseUser class for making the connection between database and the user
 */
class DatabaseUser {

    private final boolean serverMode;
    private final DatabaseConnection dbConnection;

    /**
     * This Constructor is used to create the User table if it doesn't exist when you call the singleton each time.
     *
     * @param serverMode the server mode
     */
    public DatabaseUser(boolean serverMode) throws DatabaseException {
        this.serverMode = serverMode;
        dbConnection = new DatabaseConnection();
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table if not exists User (nickname VARCHAR(255)," +
                    "password VARCHAR(255)," +
                    "PRIMARY KEY (nickname));"
            );
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to insert a new user in the database
     *
     * @param username of the user to insert
     * @param password of the user to insert
     * @throws DatabaseException if the insertion failed
     */
    public void insertNewUser(String username, String password) throws DatabaseException {
        // checks if the username is already used
        try {
            userExists(username);
        } catch (UserException noSuchUser) {    // we want to insert only if there is no user with that username
            try {
                Connection connection = dbConnection.startConnection(serverMode);
                // prepared statements prevent sql injections
                PreparedStatement preparedStatement = connection.prepareStatement("insert into User values(?,?)");
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.executeUpdate();

                connection.close();
                return; // the return is needed because we will raise an already_exist exception
            } catch (SQLException e) {
                throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
            }
        }
        // if we don't enter the catch it means that the username is already in use
        throw new UserException(DatabaseExceptionType.ALREADY_EXISTS, username);
    }

    /**
     * Method used to check if a user exists or not
     *
     * @param username to check
     * @throws DatabaseException if the user doesn't exist
     */
    public void userExists(String username) throws DatabaseException {
        // we are not throwing any exception because the method already returns a boolean telling us if true the user
        // exists else it doesn't
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            // prepared statement is used to prevent sql injections
            PreparedStatement preparedStatement = connection.prepareStatement("select 1 from User where nickname=?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            int result = resultSet.getInt(1);
            connection.close();

            if (result != 1) throw new UserException(DatabaseExceptionType.NO_SUCH_ENTRY, username);
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to delete a user from the database
     *
     * @param username to delete
     * @throws DatabaseException if the deletion failed
     */
    public void deleteUser(String username) throws DatabaseException {
        userExists(username);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            // prepared statement prevents sql injections
            PreparedStatement preparedStatement = connection.prepareStatement("delete from User where nickname=?");
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to modify the password of a certain user
     *
     * @param username    of user whose password needs to be changed
     * @param newPassword replacing the old one
     * @throws DatabaseException if the update fails
     */
    public void modifyUserPassword(String username, String newPassword) throws DatabaseException {
        userExists(username);
        Connection connection = dbConnection.startConnection(serverMode);
        try {
            // prepared statement is used to prevent sql injections
            PreparedStatement preparedStatement = connection.prepareStatement("update User set password=? where nickname=?");
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }


    /**
     * Method used to get the user from its username
     *
     * @param username of the user to fetch
     * @return the User object corresponding
     * @throws DatabaseException if fetching the user fails
     */
    public User getUser(String username) throws DatabaseException {
        userExists(username);
        try {
            Connection connection = dbConnection.startConnection(serverMode);
            // prepared statement is used to prevent sql injections
            PreparedStatement preparedStatement = connection.prepareStatement("select * from User where nickname=?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = new User(resultSet.getString(1), resultSet.getString(2));
            connection.close();
            return user;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

    /**
     * Method used to check if the user that is trying to log in is using good credentials
     *
     * @param user that is trying to log in
     * @return true if the user is valid else false
     */
    public boolean isLoginValid(User user) {
        try {
            userExists(user.getUsername());
            return getUser(user.getUsername()).equals(user);
        } catch (DatabaseException e) {
            return false;
        }
    }
}
