package ulb.infof307.g9.server;

import com.google.gson.Gson;
import ulb.infof307.g9.model.Deck;
import ulb.infof307.g9.model.RankRow;
import ulb.infof307.g9.model.User;
import ulb.infof307.g9.view.AlertBuilder;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * The Client class is used to communicate with the server.
 * It uses a OnEventListener to define the behavior in regard to certain events.
 */
public class Client {

    public static final int PORT = 8080;
    private final Socket client;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    private final OnEventListener onEventListener;

    /**
     * Constructor method initializing the connection to the server
     *
     * @param onEventListener Listener class to override for defining behavior in regard to certain events
     */
    public Client(OnEventListener onEventListener) throws IOException {
        this.onEventListener = onEventListener;
        try {
            // connection setup
            this.client = new Socket("localhost", PORT);
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));    // msg from server
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));  // msg to server
        } catch (IOException e) {
            throw new IOException("Le serveur n'est pas joignable");   // caught when initializing the class
        }
        listen();   // runs the listener on another thread
    }

    /**
     * Starts listening for messages from the server on a different thread
     */
    private void listen() {
        new Thread(() -> {
            String message;
            Gson gson = new Gson();
            Packet packet;
            while (client.isConnected()) {
                try {
                    message = bufferedReader.readLine();
                    packet = gson.fromJson(message, Packet.class);
                    if (packet == null) {
                        terminate();
                        return;
                    }

                    switch (packet.getType()) {
                        case OK -> onEventListener.onOkEvent();
                        case FAIL -> onEventListener.onFailEvent();
                        case SEND_ALL_DECKS -> onEventListener.onSendAllDecksEvent(packet.getDecks());
                        case GET_RANKING -> onEventListener.onGetRankingEvent(packet.getLeaderboard());
                    }
                } catch (IOException e) {
                    new AlertBuilder()
                            .error()
                            .addHeader("Erreur !")
                            .addContent("La connexion au serveur a été perdue")
                            .show();
                    return;
                }

            }
        }).start();

    }

    /**
     * Sends a String message to the server
     *
     * @param message message to send
     */
    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message + "\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Le message n'a pas pu être envoyé au serveur")
                    .show();
        }
    }

    /**
     * Wrapper for sendMessage(String)
     *
     * @param packet packet to transform to String and send to the server
     */
    public void sendPacket(Packet packet) {
        Gson gson = new Gson();
        sendMessage(gson.toJson(packet));
    }

    /**
     * Method used to send the score of a client
     *
     * @param deckName name of the deck from which to send the score
     * @param score    of a player
     * @param user     User who sends the score
     */
    public void sendScore(String deckName, int score, User user) {
        sendPacket(new PacketDirector().buildPacketSendScore(deckName, score, user));
    }

    /**
     * Method used to get the ranking of a deck
     *
     * @param deckName name of the deck from which to get the ranking
     */
    public void getLeaderBoard(String deckName) {
        sendPacket(new PacketDirector().buildPacketGetLeaderBoard(deckName));
    }

    /**
     * Queries the server for all the decks in its database
     */
    public void getAllDecks() {
        sendPacket(new PacketDirector().buildPacketGetDecks());
    }

    /**
     * Queries the server to authenticate the User login, the server can answer with OK or FAIL
     *
     * @param user User trying to log in
     */
    public void login(User user) {
        sendPacket(new PacketDirector().buildPacketLogin(user));
    }

    /**
     * Queries the server to register a new User, the server can answer with OK or FAIL
     *
     * @param user user to be registered
     */
    public void register(User user) {
        sendPacket(new PacketDirector().buildPacketRegister(user));
    }

    /**
     * Sends the server a new Deck to publish on the store
     *
     * @param deck deck to publish
     */
    public void exportDeck(Deck deck) {
        sendPacket(new PacketDirector().buildPacketExportDeck(deck));
    }

    /**
     * Queries the server to change the user password
     *
     * @param user new user credentials
     */
    public void changePassword(User user) {
        sendPacket(new PacketDirector().buildPacketChangePassword(user));
    }

    private void terminate() throws IOException {
        this.bufferedReader.close();
        this.bufferedWriter.close();
        this.client.close();
    }

    /**
     * Listener class to override depending on where it is initialized
     */
    public static class OnEventListener {
        private void printError() {
            new AlertBuilder()
                    .error()
                    .addHeader("Erreur !")
                    .addContent("Il y a eu une erreur")
                    .show();
        }

        /**
         * Called when the client receives FAIL type message
         */
        public void onFailEvent() {
            printError();
        }

        /**
         * Called when the client receives OK type message
         */
        public void onOkEvent() {
            printError();
        }

        /**
         * Called when the client receives SEND_ALL_DECKS type message
         *
         * @param decks list of decks sent by the server
         */
        public void onSendAllDecksEvent(List<Deck> decks) {
            printError();
        }

        public void onGetRankingEvent(List<RankRow> leaderboard) {
            printError();
        }
    }

}

