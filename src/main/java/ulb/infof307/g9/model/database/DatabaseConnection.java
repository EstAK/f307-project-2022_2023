package ulb.infof307.g9.model.database;

import ulb.infof307.g9.model.database.exceptions.DatabaseException;
import ulb.infof307.g9.model.database.exceptions.DatabaseExceptionType;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection class for making the connection between database
 */
class DatabaseConnection {

    private volatile static Connection connection;

    /**
     *
     * @param serverMode say if this database is also destined to the server database
     * @return connection
     * @throws DatabaseException
     */
    public Connection startConnection(boolean serverMode) throws DatabaseException {
        //We check the os in order to connect to the database because the paths are different on Windows and Linux.
        try {
            String serverDatabaseURL = "jdbc:sqlite:DataBaseServer.db";
            String databaseURL = "jdbc:sqlite:DataBase.db";
            connection = DriverManager.getConnection((serverMode) ? serverDatabaseURL : databaseURL);
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException(DatabaseExceptionType.SQL_ERROR, e);
        }
    }

}
