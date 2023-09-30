package ulb.infof307.g9.server;


import ulb.infof307.g9.model.database.Database;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Server class, run this to start the server.
 * The server will listen on port 8080.
 * The server will run on a separate thread for each new connection.
 */
public class Server {

    public static final int PORT = 8080;

    /**
     * Run this to start the server
     *
     * @param args nothing
     */
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(PORT);
        Database.getInstance().setServerMode(true); // setting the database for opening the server database used in ClientHandler
        while (!socket.isClosed()) {
            try {   // runs each new connection on a separate thread
                Thread thread = new Thread(new ClientHandler(socket.accept()));
                thread.start();

            } catch (IOException e) {
                System.out.println("the server failed to establish a connection with a certain host");
            }
        }
    }
}
